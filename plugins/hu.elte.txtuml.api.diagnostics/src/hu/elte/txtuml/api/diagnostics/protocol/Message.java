package hu.elte.txtuml.api.diagnostics.protocol;

import java.io.Serializable;

/**
 * Root type of all messages which passes through between diagnostics back-end and diagnostics client.
 * @author gerazo
 */
public class Message implements Serializable {
	private static final long serialVersionUID = -3021014219480855216L;

	public final MessageType messageType;
	public final int clientID;
	
	public Message(MessageType type, int clientID) {
		messageType = type;
		this.clientID = clientID;
	}
}
