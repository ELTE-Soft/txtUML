package hu.elte.txtuml.api.diagnostics.protocol;

import java.io.Serializable;

/**
 * Root type of all messages which passes through between DiagnosticsPlugin and DiagnosticsService.
 */
public class Message implements Serializable {
	private static final long serialVersionUID = -3021014219480855216L;

	public final MessageType messageType;
	public final int serviceInstanceID;
	
	public Message(MessageType type, int serviceInstanceID) {
		messageType = type;
		this.serviceInstanceID = serviceInstanceID;
	}
}
