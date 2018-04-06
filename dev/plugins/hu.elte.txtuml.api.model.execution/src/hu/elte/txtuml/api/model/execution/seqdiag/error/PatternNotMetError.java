package hu.elte.txtuml.api.model.execution.seqdiag.error;

import hu.elte.txtuml.api.model.execution.impl.seqdiag.Message;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;

@SequenceDiagramRelated
public class PatternNotMetError extends MessageError {

	private static final long serialVersionUID = -685965305292101682L;

	public PatternNotMetError(Message message) {
		super(message, "Message has not been sent: ", ErrorLevel.ERROR);
	}

}
