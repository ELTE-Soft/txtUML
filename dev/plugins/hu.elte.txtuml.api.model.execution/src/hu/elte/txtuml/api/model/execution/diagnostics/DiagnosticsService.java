package hu.elte.txtuml.api.model.execution.diagnostics;

import static hu.elte.txtuml.api.model.external.ModelClasses.getIdentifierOf;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Optional;
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
 * sends information to the DiagnosticsPlugin about events happening. Blocks
 * execution until animation is in progress (will be optional later).
 * 
 * Also stores the current active states and serves them over HTTP with a
 * {@link DiagnosticsServer}.
 */
public class DiagnosticsService extends NotifierOfTermination implements TraceListener {

	private static final String NO_PORT_SET = "No port set";

	private final int serviceInstanceID;

	private final int diagnosticsSocketPort;
	private volatile int faultTolerance = 17;

	private DiagnosticsServer server = new DiagnosticsServer();

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

		int socketPort, httpPort;
		try {
			socketPort = getPort(GlobalSettings.TXTUML_DIAGNOSTICS_SOCKET_PORT_KEY);
			httpPort = getPort(GlobalSettings.TXTUML_DIAGNOSTICS_HTTP_PORT_KEY);
		} catch (IOException e) {
			Logger.sys.error("Properties " + GlobalSettings.TXTUML_DIAGNOSTICS_SOCKET_PORT_KEY + " and "
					+ GlobalSettings.TXTUML_DIAGNOSTICS_HTTP_PORT_KEY
					+ " are not correctly set on this VM, no txtUML diagnostics will be available for service instance 0x"
					+ Integer.toHexString(serviceInstanceID));

			diagnosticsSocketPort = -1;
			notifyAllOfTermination();
			return;
		}

		try {
			server.start(httpPort);
		} catch (IOException e) {
			Logger.sys.error("Couldn't start HTTP server on port " + httpPort + " in service instance 0x"
					+ Integer.toHexString(serviceInstanceID), e);

			diagnosticsSocketPort = -1;
			notifyAllOfTermination();
			return;
		}

		diagnosticsSocketPort = socketPort;
		addTerminationListener(() -> sendMessage(new Message(MessageType.CHECKOUT, serviceInstanceID)));

		Logger.sys.info("txtUML diagnostics connection is set on socket port " + diagnosticsSocketPort
				+ " and HTTP port " + httpPort + " for service instance 0x" + Integer.toHexString(serviceInstanceID));
		sendMessage(new Message(MessageType.CHECKIN, serviceInstanceID));
	}

	private int getPort(String property) throws IOException {
		String portStr = System.getProperty(property, NO_PORT_SET);
		if (portStr.equals(NO_PORT_SET)) {
			throw new IOException();
		}

		int port;
		try {
			port = Integer.decode(portStr).intValue();
		} catch (Exception ex) {
			throw new IOException();
		}

		if (port <= 0 || port > 65535) {
			throw new IOException();
		}

		return port;
	}

	public void shutdown() {
		server.stop();
		notifyAllOfTermination();
	}

	@Override
	public void processingSignal(ModelClass object, Signal signal, Optional<ModelClass> sender) {
		sendNewModelEvent(MessageType.PROCESSING_SIGNAL, object.getClass().getCanonicalName(), getIdentifierOf(object),
				signal.getClass().getCanonicalName());
	}

	@Override
	public void usingTransition(ModelClass object, Transition transition) {
		String transitionName = transition.getClass().getCanonicalName();
		server.register(object, transitionName);
		sendNewModelEvent(MessageType.USING_TRANSITION, object.getClass().getCanonicalName(), getIdentifierOf(object),
				transitionName);
	}

	@Override
	public void enteringVertex(ModelClass object, Vertex vertex) {
		String vertexName = vertex.getClass().getCanonicalName();
		server.register(object, vertexName);
		sendNewModelEvent(MessageType.ENTERING_VERTEX, object.getClass().getCanonicalName(), getIdentifierOf(object),
				vertexName);

	}

	@Override
	public void leavingVertex(ModelClass object, Vertex vertex) {
		String vertexName = vertex.getClass().getCanonicalName();
		server.register(object, vertexName);
		sendNewModelEvent(MessageType.LEAVING_VERTEX, object.getClass().getCanonicalName(), getIdentifierOf(object),
				vertexName);
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

		try (Socket socket = new Socket(InetAddress.getLoopbackAddress(), diagnosticsSocketPort);
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
