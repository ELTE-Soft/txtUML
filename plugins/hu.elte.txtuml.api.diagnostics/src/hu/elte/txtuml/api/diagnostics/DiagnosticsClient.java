package hu.elte.txtuml.api.diagnostics;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

import hu.elte.txtuml.api.diagnostics.protocol.GlobalSettings;
import hu.elte.txtuml.api.diagnostics.protocol.Message;
import hu.elte.txtuml.api.diagnostics.protocol.MessageType;
import hu.elte.txtuml.api.diagnostics.protocol.ModelEvent;
import hu.elte.txtuml.utils.NotifierOfTermination;

/**
 * Registers itself at ModelExecutor.Report as a ModelExecutionEventsListener
 * and sends information the the diagnostics back-end about events happening.
 * Blocks execution until animation is in progress (will be optional later).
 * Has a singleton lifecycle and can be shut down.
 * @author gerazo
 */
public class DiagnosticsClient extends NotifierOfTermination {

	private static final String NO_PORT_SET = "No port set";
	
	private static int clientID = 0;
	private static volatile DiagnosticsClient instance;
	
	private final int diagnosticsPort;
	private int faultTolerance = 17;

	/**
	 * Initiates singleton by signaling the presence of a new client towards the back-end.
	 * It also does configuration if needed.
	 */
	public static final void startInstance() {
		getInstance();
	}

	/**
	 * Initiates singleton first if needed by signaling the presence of a new client towards the back-end.
	 * It also does configuration if needed.
	 * @return DiagnosticsClient singleton
	 */
	public static final DiagnosticsClient getInstance() {
		if (instance == null) {
			synchronized (DiagnosticsClient.class) {
				if (instance == null) {
					while (clientID == 0) {
						clientID = new Random().nextInt();
					}
					instance = new DiagnosticsClient();
				}
			}
		}
		return instance;
	}
	
	/**
	 * Shuts down singleton instance to signal to the back-end that the diagnostics session is no longer active.
	 */
	public static final void shutdownInstance() {
		if (instance != null) {
			synchronized (DiagnosticsClient.class) {
				if (instance != null) {
					instance.shutdown();
					instance = null;
				}
			}
		}
	}
	
	public void sendNewModelEvent(MessageType type, String modelClassName,
			String modelClassInstanceID, String eventTargetClassName,
			boolean waitForAcknowledgement) {

		sendMessage(new ModelEvent(type, clientID, modelClassName,
				modelClassInstanceID, eventTargetClassName),
				waitForAcknowledgement);
	}
	
	private DiagnosticsClient() {
		String portStr = System.getProperty(GlobalSettings.TXTUML_DIAGNOSTICS_PORT_KEY, NO_PORT_SET);
		int port = -1;
		if (portStr.equals(NO_PORT_SET)) {
			diagnosticsPort = -1;
			notifyAllOfTermination();
			return;
		}
		try {
			port = Integer.decode(portStr).intValue();
		} catch (Exception ex) {}
		if (port <= 0 || port > 65535) {
			System.out.println("INFO: No " + GlobalSettings.TXTUML_DIAGNOSTICS_PORT_KEY + " property is set correctly on this VM, no txtUML diagnostics will be available for client 0x" + Integer.toHexString(clientID));
			diagnosticsPort = -1;
			notifyAllOfTermination();
			return;
		}
		diagnosticsPort = port;
		
		System.out.println("INFO: txtUML Diagnostics connection is set on " + diagnosticsPort + " for client 0x" + Integer.toHexString(clientID));
		sendMessage(new Message(MessageType.CHECKIN, clientID), false);
	}
	
	private void shutdown() {
		if (diagnosticsPort != -1) {
			notifyAllOfTermination();
			sendMessage(new Message(MessageType.CHECKOUT, clientID), false);
		}
	}

	private void sendMessage(Message message, boolean waitForAcknowledgement) {
		try (
			Socket socket = new Socket(InetAddress.getLoopbackAddress(), diagnosticsPort);
		) {
			assert socket.isBound() && socket.isConnected();
			ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
			outStream.writeObject(message);
			outStream.flush();
			if (waitForAcknowledgement) {
				ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
				Message ack = null;
				try {
					ack = (Message)inStream.readObject();
				} catch (ClassNotFoundException | ClassCastException ex) {
					if (faultTolerance > 0) {
						faultTolerance--;
						System.out.println("ERROR: communication problem in client 0x" + Integer.toHexString(clientID) + ": " + ex);
					}
					assert false;
				}
				assert ack != null && ack.messageType == MessageType.ACKNOWLEDGED;
			}
		} catch (IOException ex) {
			if (faultTolerance > 0) {
				faultTolerance--;
				System.out.println("ERROR: communication problem in client 0x" + Integer.toHexString(clientID) + ": " + ex + " at ");
				ex.printStackTrace();
			}
			assert false;
		}
		if (faultTolerance == 0) {
			System.out.println("WARN: Something is fishy with the diagnostics connection, no more log poisoning, no more guarantees...");
			faultTolerance = -1;
		}
	}

}
