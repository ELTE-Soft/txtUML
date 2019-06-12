package hu.elte.txtuml.api.model.execution.seqdiag.error;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.execution.impl.seqdiag.Message;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;

/**
 * General invalid message error.
 */
@SequenceDiagramRelated
public class InvalidMessageError<T extends ModelClass, U extends ModelClass> extends MessageError<T, U> {

	public InvalidMessageError(Message<T, U> message, ErrorLevel level) {
		super(message, "Invalid message sent: ", level);
	}

	public InvalidMessageError(Message<T, U> message, String concreteError, ErrorLevel level) {
		super(message, concreteError, level);
	}

}