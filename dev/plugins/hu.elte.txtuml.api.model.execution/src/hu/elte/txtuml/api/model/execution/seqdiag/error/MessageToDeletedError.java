package hu.elte.txtuml.api.model.execution.seqdiag.error;

import hu.elte.txtuml.api.model.execution.impl.seqdiag.Message;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;

/**
 * Invalid message error, which occurs when a message is sent to a deleted
 * target.
 */
@SequenceDiagramRelated
public class MessageToDeletedError extends InvalidMessageError {

	private static final long serialVersionUID = -7852238254689099682L;

	public MessageToDeletedError(Message message) {
		super(message, "Message sent to deleted target: ", ErrorLevel.ERROR);
	}

}
