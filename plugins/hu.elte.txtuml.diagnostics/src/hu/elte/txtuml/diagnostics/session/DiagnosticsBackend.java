package hu.elte.txtuml.diagnostics.session;

import hu.elte.txtuml.api.diagnostics.protocol.Message;
import hu.elte.txtuml.api.diagnostics.protocol.MessageType;
import hu.elte.txtuml.api.diagnostics.protocol.ModelEvent;
import hu.elte.txtuml.diagnostics.Activator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * Receives DiagnosticsClient events and handles them accordingly.
 * Drives animator and blocks client on the other side.
 * @author gerazo
 */
public class DiagnosticsBackend implements IBackend, Runnable {

	private static final int SERVER_SOCKET_BACKLOG = 50;
	
	private Thread thread;
	private volatile boolean shutdownHasCome = false;
	private ServerSocket serverSocket;
	private Animator animator;
	
	public DiagnosticsBackend(int diagnosticsPort, String projectName, String workingDirectory) throws IOException {
		try {
			serverSocket = new ServerSocket(diagnosticsPort, SERVER_SOCKET_BACKLOG, InetAddress.getLoopbackAddress());
		} catch (IOException | IllegalArgumentException | SecurityException ex) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Problem creating server socket: " + ex));
			throw ex;
		}
		animator = new Animator(projectName);
		thread = new Thread(this, "txtUMLDiagnosticsBackendThread");
		thread.start();
		//Activator.getDefault().getLog().log(new Status(IStatus.INFO, Activator.PLUGIN_ID, "txtUML Diagnostics Backend started"));
	}
	
	@Override
	public void shutdown() {
		shutdownHasCome = true;
		thread.interrupt();
		try {
			serverSocket.close();
		} catch (IOException ex) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Problem shutting down server socket: " + ex));
			assert false;
		}
		try {
			thread.join();
		} catch (InterruptedException ex) {}
		assert !thread.isAlive() : "ERROR: Failed to shut server thread down";
		thread = null;
		animator.dispose();
		animator = null;
	}

	@Override
	public void run() {
		int faultTolerance = 37;
		while (!shutdownHasCome) {
			Socket socket = null;
			try {
				socket = serverSocket.accept();
			} catch (IOException ex) {
				if (!shutdownHasCome && faultTolerance > 0) {
					Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Problem with reception from client: " + ex));
					faultTolerance--;
					assert false;
				}
			}
			
			if (socket != null) {
				assert socket.isConnected() && !socket.isClosed();
				try {
					ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
					Message event = null;
					try {
						event = (Message)inStream.readObject();
					} catch (ClassNotFoundException | ClassCastException ex) {
						if (!shutdownHasCome && faultTolerance > 0) {
							Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Protocol problem: " + ex));
							faultTolerance--;
							assert false;
						}
					}
					
					if (event != null) {
						boolean sendAck = false;
						/*if (event.messageType == MessageType.CHECKIN ||
								event.messageType == MessageType.CHECKOUT) {
							// handle multiple clients here
						} else*/
						if (event.messageType == MessageType.PROCESSING_SIGNAL ||
								event.messageType == MessageType.USING_TRANSITION ||
								event.messageType == MessageType.ENTERING_VERTEX ||
								event.messageType == MessageType.LEAVING_VERTEX) {
							animator.animate((ModelEvent)event);
							sendAck = true;
						}
												
						if (sendAck) {
							ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
							outStream.writeObject(new Message(MessageType.ACKNOWLEDGED, 0));
							outStream.flush();
						}
					}
				} catch (IOException ex) {
					if (!shutdownHasCome && faultTolerance > 0) {
						StringWriter sw = new StringWriter();
						ex.printStackTrace(new PrintWriter(sw));
						Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Communication problem: " + ex + " at " + sw));
						faultTolerance--;
						assert false;
					}
				} finally {
					try {
						socket.close();
					} catch (IOException ex) {}
				}
			}
			
			if (faultTolerance == 0) {
				Activator.getDefault().getLog().log(new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Something is fishy with the diagnostics connection, no more log poisoning, no more guarantees..."));
				faultTolerance = -1;
			}
		}
	}

}
