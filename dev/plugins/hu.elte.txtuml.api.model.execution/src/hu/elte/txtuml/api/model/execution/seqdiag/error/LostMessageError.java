package hu.elte.txtuml.api.model.execution.seqdiag.error;

import hu.elte.txtuml.api.model.execution.impl.seqdiag.Message;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;

@SequenceDiagramRelated
public class LostMessageError extends InvalidMessageError {

	private static final long serialVersionUID = -5784641092185110713L;

	public LostMessageError(Message message) {
		super(message, "Target did not consume the message: ", ErrorLevel.WARNING);
	}

}
