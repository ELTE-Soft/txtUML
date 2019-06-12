package hu.elte.txtuml.api.model.execution.seqdiag.error;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.execution.impl.seqdiag.Message;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;

/**
 * Invalid message error, which occurs when target did not consume the message.
 */
@SequenceDiagramRelated
public class LostMessageError<T extends ModelClass, U extends ModelClass> extends InvalidMessageError<T, U> {

	public LostMessageError(Message<T, U> message) {
		super(message, "Target did not consume the message: ", ErrorLevel.WARNING);
	}

}
