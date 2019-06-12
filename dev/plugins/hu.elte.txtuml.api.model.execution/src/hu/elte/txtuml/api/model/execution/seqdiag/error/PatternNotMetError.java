package hu.elte.txtuml.api.model.execution.seqdiag.error;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.execution.impl.seqdiag.Message;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;

/**
 * Occurs when an expected message has not been sent in the model.
 */
@SequenceDiagramRelated
public class PatternNotMetError<T extends ModelClass, U extends ModelClass> extends MessageError<T, U> {

	public PatternNotMetError(Message<T, U> message) {
		super(message, "Message has not been sent: ", ErrorLevel.ERROR);
	}

}
