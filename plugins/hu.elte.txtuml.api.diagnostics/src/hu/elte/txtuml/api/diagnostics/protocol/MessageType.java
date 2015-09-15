package hu.elte.txtuml.api.diagnostics.protocol;

/**
 * Helps non-java serialization for clients lacking RTTI.
 * @author gerazo
 */
public enum MessageType {
	ACKNOWLEDGED,
	CHECKIN,
	CHECKOUT,
	PROCESSING_SIGNAL,
	USING_TRANSITION,
	ENTERING_VERTEX,
	LEAVING_VERTEX
}
