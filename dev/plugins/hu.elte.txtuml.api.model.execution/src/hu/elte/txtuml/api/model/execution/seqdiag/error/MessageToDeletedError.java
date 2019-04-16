package hu.elte.txtuml.api.model.execution.seqdiag.error;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.execution.impl.seqdiag.Message;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;

/**
 * Invalid message error, which occurs when a message is sent to a deleted
 * target.
 */
@SequenceDiagramRelated
public class MessageToDeletedError<T extends ModelClass, U extends ModelClass> extends InvalidMessageError<T, U> {

	public MessageToDeletedError(Message<T, U> message) {
		super(message, "Message sent to deleted target: ", ErrorLevel.ERROR);
	}

}
