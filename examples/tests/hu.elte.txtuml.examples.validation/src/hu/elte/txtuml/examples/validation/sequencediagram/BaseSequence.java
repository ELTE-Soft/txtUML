package hu.elte.txtuml.examples.validation.sequencediagram;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.seqdiag.Position;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import hu.elte.txtuml.api.model.seqdiag.Sequence;
import hu.elte.txtuml.api.model.seqdiag.Lifeline;
import hu.elte.txtuml.examples.validation.sequencediagram.testmodel.A;
import hu.elte.txtuml.examples.validation.sequencediagram.testmodel.AToB;
import hu.elte.txtuml.examples.validation.sequencediagram.testmodel.B;

public abstract class BaseSequence extends SequenceDiagram {

	@Position(10)
	protected Lifeline<A> lifeline1;

	public Lifeline<B> lifeline2;

	@Override
	public void initialize() {
		A a = Action.create(A.class);
		B b = new B();
		Action.link(AToB.ASide.class, a, AToB.BSide.class, b);
		lifeline1 = Sequence.createLifeline(a);
		lifeline2 = Sequence.createLifeline(b);
	}

}
