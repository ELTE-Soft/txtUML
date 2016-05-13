package hu.elte.txtuml.api.model.execution.diagnostics.protocol;

/**
 * Instance life-cycle related event
 */
public class InstanceEvent extends Message {
	private static final long serialVersionUID = -5803880169919413931L;

	public final String modelClassName;
	public final String modelClassInstanceID;

	public InstanceEvent(MessageType type, int serviceInstanceID, String modelClassName, String modelClassInstanceID) {
		super(type, serviceInstanceID);
		assert type == MessageType.INSTANCE_CREATION || type == MessageType.INSTANCE_DESTRUCTION
				|| type == MessageType.PROCESSING_SIGNAL || type == MessageType.USING_TRANSITION
				|| type == MessageType.ENTERING_VERTEX || type == MessageType.LEAVING_VERTEX;
		this.modelClassName = modelClassName;
		this.modelClassInstanceID = modelClassInstanceID;
	}

}
