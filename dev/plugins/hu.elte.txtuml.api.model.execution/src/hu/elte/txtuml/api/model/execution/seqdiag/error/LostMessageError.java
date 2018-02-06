package hu.elte.txtuml.api.model.execution.seqdiag.error;

import hu.elte.txtuml.api.model.execution.impl.seqdiag.Message;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;

@SequenceDiagramRelated
@SuppressWarnings("serial")
public class LostMessageError extends InvalidMessageError {

	public LostMessageError(Message message) {
		super(message, "It sent but the target did not consume:");
	}

}
