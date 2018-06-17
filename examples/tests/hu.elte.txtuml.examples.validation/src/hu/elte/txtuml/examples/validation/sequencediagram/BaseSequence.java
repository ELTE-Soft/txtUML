package hu.elte.txtuml.examples.validation.sequencediagram;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.examples.validation.sequencediagram.testmodel.A;
import hu.elte.txtuml.examples.validation.sequencediagram.testmodel.AToB;
import hu.elte.txtuml.examples.validation.sequencediagram.testmodel.B;
import hu.elte.txtuml.examples.validation.sequencediagram.testmodel.TestSig;
import hu.elte.txtuml.api.model.seqdiag.Position;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;

public abstract class BaseSequence extends SequenceDiagram {

	@Position(10)
	protected A lifeline1;

	public B lifeline2;

	@Override
	public void initialize() {
		lifeline1 = Action.create(A);
		lifeline2 = new B();
		Action.link(AToB.ASide.class, lifeline1, AToB.BSide.class, lifeline2);
	}

}
