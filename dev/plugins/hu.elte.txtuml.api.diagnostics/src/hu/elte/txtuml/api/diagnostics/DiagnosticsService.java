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
import hu.elte.txtuml.utils.Logger;
import hu.elte.txtuml.utils.NotifierOfTermination;

/**
 * Registers itself at ModelExecutor.Report as a ModelExecutionEventsListener
 * and sends information the the DiagnosticsPlugin about events happening.
 * Blocks execution until animation is in progress (will be optional later).
 * Has a singleton lifecycle and can be shut down.
 */
public class DiagnosticsService extends NotifierOfTermination {

	private static final String NO_PORT_SET = "No port set";
	
	private static int serviceInstanceID = 0;
	private static volatile DiagnosticsService instance;
	
	private final int diagnosticsPort;
	private int faultTolerance = 17;

	/**
	 * Initiates singleton by signaling the presence of a new DiagnosticsService towards the DiagnosticsPlugin.
	 * It also does configuration if needed.
	 */
	public static final void startInstance() {
		getInstance();
	}

	/**
	 * Initiates singleton first if needed by signaling the presence of a new DiagnosticsService towards the DiagnosticsPlugin.
	 * It also does configuration if needed.
	 * @return DiagnosticsService singleton
	 */
	public static final DiagnosticsService getInstance() {
		if (instance == null) {
			synchronized (DiagnosticsService.class) {
				if (instance == null) {
					while (serviceInstanceID == 0) {
						serviceInstanceID = new Random().nextInt();
					}
					instance = new DiagnosticsService();
				}
			}
		}
		return instance;
	}
	
	/**
	 * Shuts down singleton instance to signal to the DiagnosticsPlugin that the diagnostic session is no longer active.
	 */
	public static final void shutdownInstance() {
		if (instance != null) {
			synchronized (DiagnosticsService.class) {
				if (instance != null) {
					instance.shutdown();
					instance = null;
				}
			}
		}
	}
	
	public void sendNewModelEvent(MessageType type, String modelClassName,
			String modelClassInstanceID, String eventTargetClassName) {
		sendMessage(new ModelEvent(type, serviceInstanceID, modelClassName,
				modelClassInstanceID, eventTargetClassName));
	}
	
	private DiagnosticsService() {
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
			Logger.sys.info("INFO: No " + GlobalSettings.TXTUML_DIAGNOSTICS_PORT_KEY + " property is set correctly on this VM, no txtUML diagnostics will be available for service instance 0x" + Integer.toHexString(serviceInstanceID));
			diagnosticsPort = -1;
			notifyAllOfTermination();
			return;
		}
		diagnosticsPort = port;
		
		Logger.sys.info("txtUML Diagnostics connection is set on " + diagnosticsPort + " for service instance 0x" + Integer.toHexString(serviceInstanceID));
		sendMessage(new Message(MessageType.CHECKIN, serviceInstanceID));
	}
	
	private void shutdown() {
		if (diagnosticsPort != -1) {
			notifyAllOfTermination();
			sendMessage(new Message(MessageType.CHECKOUT, serviceInstanceID));
		}
	}

	private void sendMessage(Message message) {
		try (
			Socket socket = new Socket(InetAddress.getLoopbackAddress(), diagnosticsPort);
			ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
		) {
			assert socket.isBound() && socket.isConnected();
			outStream.writeObject(message);
			outStream.flush();
			if (message.messageType.isAckNeeded()) {
				Message ack = null;
				try (
					ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
				) {
					ack = (Message)inStream.readObject();
				} catch (ClassNotFoundException | ClassCastException ex) {
					if (faultTolerance > 0) {
						faultTolerance--;
						Logger.sys.error("Communication problem in service instance 0x" + Integer.toHexString(serviceInstanceID), ex);
					}
					assert false;
				}
				assert ack != null && ack.messageType == MessageType.ACKNOWLEDGED;
			}
		} catch (IOException ex) {
			if (faultTolerance > 0) {
				faultTolerance--;
				Logger.sys.error("Communication problem in service instance 0x" + Integer.toHexString(serviceInstanceID), ex);
			}
			assert false;
		}
		if (faultTolerance == 0) {
			Logger.sys.warn("Something is fishy with the diagnostics connection, no more log poisoning, no more guarantees...");
			faultTolerance = -1;
		}
	}

}
