package hu.elte.txtuml.diagnostics.session;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

import hu.elte.txtuml.api.model.execution.diagnostics.protocol.Message;
import hu.elte.txtuml.api.model.execution.diagnostics.protocol.MessageType;
import hu.elte.txtuml.api.model.execution.diagnostics.protocol.ModelEvent;
import hu.elte.txtuml.diagnostics.animation.js.DiagnosticsServer;
import hu.elte.txtuml.diagnostics.animation.papyrus.Animator;
import hu.elte.txtuml.utils.Logger;

/**
 * Receives DiagnosticsService events and handles them accordingly.
 * Drives animator and blocks client service on the other side.
 */
public class DiagnosticsPlugin implements IDisposable, Runnable {

	private static final int SERVER_SOCKET_BACKLOG = 50;
	private static final int FAULT_TOLERANCE = 99;
	private static AtomicInteger DELAY = new AtomicInteger(5000);
	
	private Thread thread;
	private volatile boolean shutdownHasCome = false;
	private ServerSocket serverSocket;
	private ModelMapper modelMapper;
	private InstanceRegister instanceRegister;
	private Animator animator;
	private DiagnosticsServer server;
	
	public DiagnosticsPlugin(int diagnosticsPort, int httpPort, String projectName, String workingDirectory) throws IOException {
		try {
			serverSocket = new ServerSocket(diagnosticsPort, SERVER_SOCKET_BACKLOG, InetAddress.getLoopbackAddress());
		} catch (IOException | IllegalArgumentException | SecurityException ex) {
			Logger.sys.error("Problem creating server socket: " + ex);
			throw ex;
		}
		modelMapper = new ModelMapper(projectName);
		instanceRegister = new InstanceRegister();
		animator = new Animator(instanceRegister, modelMapper);
		server = new DiagnosticsServer(this);
		
		try {
			server.start(httpPort);
		} catch (IOException e) {
			Logger.sys.error("Couldn't start HTTP server on port " + httpPort + " in service instance 0x", e);
			return;
		}
		Logger.sys.info("txtUML diagnostics connection is set on HTTP port " + httpPort );
		
		thread = new Thread(this, "txtUMLDiagnosticsPlugin");
		thread.start();
		//Logger.logInfo("txtUML DiagnosticsPlugin started"));
	}
	
	public void setDelay(int speed){
		DELAY = new AtomicInteger(speed);
	}
	
	public int getDelay(){
		return DELAY.intValue();
	}
	
	@Override
	public void dispose() {
		shutdownHasCome = true;
		thread.interrupt();
		try {
			serverSocket.close();
		} catch (IOException ex) {
			Logger.sys.error("Problem shutting down server socket", ex);
			assert false;
		}
		try {
			thread.join();
		} catch (InterruptedException ex) {}
		assert !thread.isAlive() : "ERROR: Failed to shut server thread down";
		thread = null;
		serverSocket = null;

		animator.dispose();
		animator = null;
		instanceRegister.dispose();
		instanceRegister = null;
		modelMapper.dispose();
		modelMapper = null;
		
		server.stop();
		server = null;
	}

	@Override
	public void run() {
		int faultTolerance = FAULT_TOLERANCE;
		while (!shutdownHasCome) {
			Socket socket = null;
			try {
				socket = serverSocket.accept();
			} catch (IOException ex) {
				if (!shutdownHasCome && faultTolerance > 0) {
					Logger.sys.error("Problem with reception from client service", ex);
					faultTolerance--;
					assert false;
				}
			}
			
			if (socket != null) {
				assert socket.isConnected() && !socket.isClosed();
				try (
					ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream())
				) {

					Message event = null;
					try {
						event = (Message)inStream.readObject();
					} catch (ClassNotFoundException | ClassCastException ex) {
						if (!shutdownHasCome && faultTolerance > 0) {
							Logger.sys.error("Protocol problem", ex);
							faultTolerance--;
							assert false;
						}
					}
					
					if (event != null) {
						instanceRegister.processMessage(event);
						if (event instanceof ModelEvent) {
							animator.animateEvent((ModelEvent)event);
							server.animateEvent((ModelEvent)event);
							try {
								Thread.sleep(DELAY.longValue());
							} catch (InterruptedException ex) {}
						}
												
						if (event.messageType.isAckNeeded()) {
							try (
								ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());	
							) {
								outStream.writeObject(new Message(MessageType.ACKNOWLEDGED, 0));
								outStream.flush();
							}
						}
					}
				} catch (IOException ex) {
					if (!shutdownHasCome && faultTolerance > 0) {
						try (
							StringWriter sw = new StringWriter();
							PrintWriter pw = new PrintWriter(sw);
						) {
							Logger.sys.error("Communication problem: ", ex);
							faultTolerance--;
							assert false;
						} catch (IOException e) {}
					}
				} finally {
					try {
						socket.close();
					} catch (IOException ex) {}
				}
			}
			
			if (faultTolerance == 0) {
				Logger.sys.warn("Something is fishy with the diagnostics connection, no more log poisoning, no more guarantees...");
				faultTolerance = -1;
			}
		}
	}

}
