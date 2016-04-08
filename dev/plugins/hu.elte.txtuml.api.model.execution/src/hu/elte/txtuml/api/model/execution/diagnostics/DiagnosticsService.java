package hu.elte.txtuml.api.model.execution.diagnostics;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.StateMachine.Transition;
import hu.elte.txtuml.api.model.StateMachine.Vertex;
import hu.elte.txtuml.api.model.execution.ModelExecutor;
import hu.elte.txtuml.api.model.execution.TraceListener;
import hu.elte.txtuml.api.model.execution.diagnostics.protocol.GlobalSettings;
import hu.elte.txtuml.api.model.execution.diagnostics.protocol.Message;
import hu.elte.txtuml.api.model.execution.diagnostics.protocol.MessageType;
import hu.elte.txtuml.api.model.execution.diagnostics.protocol.ModelEvent;
import hu.elte.txtuml.utils.Logger;
import hu.elte.txtuml.utils.NotifierOfTermination;

/**
 * Registers itself as a {@link TraceListener} in a {@link ModelExecutor} and
 * sends information the the DiagnosticsPlugin about events happening. Blocks
 * execution until animation is in progress (will be optional later).
 */
public class DiagnosticsService extends NotifierOfTermination implements TraceListener {

	private static final String NO_PORT_SET = "No port set";

	private final int serviceInstanceID;

	private final int diagnosticsPort;
	private volatile int faultTolerance = 17;

	/**
	 * Initiates singleton by signaling the presence of a new DiagnosticsService
	 * towards the DiagnosticsPlugin. It also does configuration if needed.
	 */
	public DiagnosticsService() {
		int rnd;
		do {
			rnd = new Random().nextInt();
		} while (rnd == 0);
		serviceInstanceID = rnd;

		String portStr = System.getProperty(GlobalSettings.TXTUML_DIAGNOSTICS_PORT_KEY, NO_PORT_SET);
		int port = -1;
		if (portStr.equals(NO_PORT_SET)) {
			diagnosticsPort = -1;
			notifyAllOfTermination();
			return;
		}
		try {
			port = Integer.decode(portStr).intValue();
		} catch (Exception ex) {
		}
		if (port <= 0 || port > 65535) {
			Logger.sys.info("INFO: No " + GlobalSettings.TXTUML_DIAGNOSTICS_PORT_KEY
					+ " property is set correctly on this VM, no txtUML diagnostics will be available for service instance 0x"
					+ Integer.toHexString(serviceInstanceID));
			diagnosticsPort = -1;
			notifyAllOfTermination();
			return;
		}
		diagnosticsPort = port;

		addTerminationListener(() -> sendMessage(new Message(MessageType.CHECKOUT, serviceInstanceID)));

		Logger.sys.info("txtUML Diagnostics connection is set on " + diagnosticsPort + " for service instance 0x"
				+ Integer.toHexString(serviceInstanceID));
		sendMessage(new Message(MessageType.CHECKIN, serviceInstanceID));
	}

	public void shutdown() {
		notifyAllOfTermination();
	}

	@Override
	public void processingSignal(ModelClass object, Signal signal) {
		sendNewModelEvent(MessageType.PROCESSING_SIGNAL, object.getClass().getCanonicalName(),
				object.runtimeInfo().getIdentifier(), signal.getClass().getCanonicalName());
	}

	@Override
	public void usingTransition(ModelClass object, Transition transition) {
		sendNewModelEvent(MessageType.USING_TRANSITION, object.getClass().getCanonicalName(),
				object.runtimeInfo().getIdentifier(), transition.getClass().getCanonicalName());
	}

	@Override
	public void enteringVertex(ModelClass object, Vertex vertex) {
		sendNewModelEvent(MessageType.ENTERING_VERTEX, object.getClass().getCanonicalName(),
				object.runtimeInfo().getIdentifier(), vertex.getClass().getCanonicalName());
	}

	@Override
	public void leavingVertex(ModelClass object, Vertex vertex) {
		sendNewModelEvent(MessageType.LEAVING_VERTEX, object.getClass().getCanonicalName(),
				object.runtimeInfo().getIdentifier(), vertex.getClass().getCanonicalName());
	}

	private void sendNewModelEvent(MessageType type, String modelClassName, String modelClassInstanceID,
			String eventTargetClassName) {
		sendMessage(
				new ModelEvent(type, serviceInstanceID, modelClassName, modelClassInstanceID, eventTargetClassName));
	}

	private void sendMessage(Message message) {
		if (isAlreadyTerminated()) {
			return;
		}

		try (Socket socket = new Socket(InetAddress.getLoopbackAddress(), diagnosticsPort);
				ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());) {
			assert socket.isBound() && socket.isConnected();
			outStream.writeObject(message);
			outStream.flush();
			if (message.messageType.isAckNeeded()) {
				Message ack = null;
				try (ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());) {
					ack = (Message) inStream.readObject();
				} catch (ClassNotFoundException | ClassCastException ex) {
					if (faultTolerance > 0) {
						faultTolerance--;
						Logger.sys.error(
								"Communication problem in service instance 0x" + Integer.toHexString(serviceInstanceID),
								ex);
					}
					assert false;
				}
				assert ack != null && ack.messageType == MessageType.ACKNOWLEDGED;
			}
		} catch (IOException ex) {
			if (faultTolerance > 0) {
				faultTolerance--;
				Logger.sys.error(
						"Communication problem in service instance 0x" + Integer.toHexString(serviceInstanceID), ex);
			}
			assert false;
		}
		if (faultTolerance == 0) {
			Logger.sys.warn(
					"Something is fishy with the diagnostics connection, no more log poisoning, no more guarantees...");
			faultTolerance = -1;
		}
	}

}
