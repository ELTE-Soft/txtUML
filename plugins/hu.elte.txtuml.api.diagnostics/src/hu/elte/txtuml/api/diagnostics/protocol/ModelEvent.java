package hu.elte.txtuml.api.diagnostics.protocol;

/**
 * General event on something in the model.
 * @author gerazo
 */
public class ModelEvent extends Message {
	private static final long serialVersionUID = 8194665363543638635L;

	public final String modelClassName;
	public final String modelClassInstanceID;
	public final String eventTargetClassName;
	
	public ModelEvent(MessageType type, int clientID,
			String modelClassName, String modelClassInstanceID, String eventTargetClassName) {
		super(type, clientID);
		assert type == MessageType.PROCESSING_SIGNAL || type == MessageType.USING_TRANSITION ||
				type == MessageType.ENTERING_VERTEX || type == MessageType.LEAVING_VERTEX;
		this.modelClassName = modelClassName;
		this.modelClassInstanceID = modelClassInstanceID;
		this.eventTargetClassName = eventTargetClassName;
	}

}
