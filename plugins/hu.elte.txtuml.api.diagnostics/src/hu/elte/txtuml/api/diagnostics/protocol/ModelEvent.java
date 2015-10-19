package hu.elte.txtuml.api.diagnostics.protocol;

/**
 * Model related event on an instance
 * @author gerazo
 */
public class ModelEvent extends InstanceEvent {
	private static final long serialVersionUID = 8194665363543638635L;

	public final String eventTargetClassName;
	
	public ModelEvent(MessageType type, int serviceInstanceID,
			String modelClassName, String modelClassInstanceID, String eventTargetClassName) {
		super(type, serviceInstanceID, modelClassName, modelClassInstanceID);
		assert type == MessageType.PROCESSING_SIGNAL || type == MessageType.USING_TRANSITION ||
				type == MessageType.ENTERING_VERTEX || type == MessageType.LEAVING_VERTEX;
		this.eventTargetClassName = eventTargetClassName;
	}

}
