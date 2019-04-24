package hu.elte.txtuml.examples.validation.sequencediagram;

import hu.elte.txtuml.api.model.seqdiag.Position;
import hu.elte.txtuml.api.model.seqdiag.Sequence;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;

public class InvalidLifelineDeclaration extends SequenceDiagram {

	class NotModelClass {
	}

	@Position(1)
	private NotModelClass lifeline1;

	@Override
	public void initialize() {
	}

	@Override
	public void run() {
		Sequence.send(null, null, null);
	}

}
