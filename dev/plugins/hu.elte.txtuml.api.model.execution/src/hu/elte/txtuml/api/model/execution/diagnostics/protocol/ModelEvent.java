package hu.elte.txtuml.api.model.execution.diagnostics.protocol;

/**
 * Model related event on an instance
 */
public class ModelEvent extends InstanceEvent {
	private static final long serialVersionUID = 8194665363543638635L;

	public final String eventTargetClassName;

	public ModelEvent(MessageType type, int serviceInstanceID, String modelClassName, String modelClassInstanceID, 
			String modelClassInstanceName, String eventTargetClassName) {
		super(type, serviceInstanceID, modelClassName, modelClassInstanceID, modelClassInstanceName);
		assert type == MessageType.PROCESSING_SIGNAL || type == MessageType.USING_TRANSITION
				|| type == MessageType.ENTERING_VERTEX || type == MessageType.LEAVING_VERTEX;
		this.eventTargetClassName = eventTargetClassName;
	}

}
