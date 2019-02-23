package hu.elte.txtuml.examples.validation.sequencediagram;

import hu.elte.txtuml.api.model.seqdiag.Position;
import hu.elte.txtuml.api.model.seqdiag.Sequence;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import hu.elte.txtuml.examples.validation.sequencediagram.testmodel.A;

public class InvalidPosition extends SequenceDiagram {

	@Position(-1)
	private A lifeline1;

	@Override
	public void initialize() {
	}

	@Override
	public void run() {
		Sequence.send(null, null, null);
	}

}
