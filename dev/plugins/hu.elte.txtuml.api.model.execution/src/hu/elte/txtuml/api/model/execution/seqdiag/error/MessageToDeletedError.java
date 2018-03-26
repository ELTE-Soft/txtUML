package hu.elte.txtuml.api.model.execution.seqdiag.error;

import hu.elte.txtuml.api.model.execution.impl.seqdiag.Message;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;

@SequenceDiagramRelated
@SuppressWarnings("serial")
public class MessageToDeletedError extends InvalidMessageError {

	public MessageToDeletedError(Message message) {
		super(message, "It sent but the target was deleted:");
	}

}
