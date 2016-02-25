package hu.elte.txtuml.api.model.execution.diagnostics.protocol;

/**
 * Helps non-java serialization for compiled code lacking RTTI.
 */
public enum MessageType {
	ACKNOWLEDGED,
	CHECKIN,
	CHECKOUT,
	INSTANCE_CREATION,
	INSTANCE_DESTRUCTION,
	PROCESSING_SIGNAL(true),
	USING_TRANSITION(true),
	ENTERING_VERTEX(true),
	LEAVING_VERTEX(true);
	
	private final boolean ackNeeded;
	
	private MessageType() {
		this.ackNeeded = false;
	}

	private MessageType(boolean ackNeeded) {
		this.ackNeeded = ackNeeded;
	}
	
	public boolean isAckNeeded() {
		return ackNeeded;
	}
}
