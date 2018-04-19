package hu.elte.txtuml.api.model.execution.seqdiag.error;

import hu.elte.txtuml.api.model.execution.impl.seqdiag.Message;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;

@SequenceDiagramRelated
@SuppressWarnings("serial")
public class InvalidMessageError extends MessageError {

	public InvalidMessageError(Message message) {
		super(message, "It sent but it should not have:");
	}

	public InvalidMessageError(Message message, String concreteError) {
		super(message, concreteError);
	}

}
