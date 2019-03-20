package hu.elte.txtuml.seqdiag.tests.models.sequences;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.seqdiag.Position;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import hu.elte.txtuml.api.model.seqdiag.Lifeline;
import hu.elte.txtuml.api.model.seqdiag.Sequence;
import hu.elte.txtuml.seqdiag.tests.models.testmodel.A;
import hu.elte.txtuml.seqdiag.tests.models.testmodel.AToB;
import hu.elte.txtuml.seqdiag.tests.models.testmodel.B;
import hu.elte.txtuml.seqdiag.tests.models.testmodel.BToC;
import hu.elte.txtuml.seqdiag.tests.models.testmodel.C;

public abstract class BaseSequence extends SequenceDiagram {
	@Position(1)
	public Lifeline<A> lifeline1;
	@Position(3)
	public Lifeline<B> lifeline2;
	@Position(2)
	public Lifeline<C> lifeline3;

	@Override
	public void initialize() {
		A a = new A();
		B b = new B();
		C c = new C();
		Action.link(AToB.ASide.class, a, AToB.BSide.class, b);
		Action.link(BToC.BSide.class, b, BToC.CSide.class, c);
		lifeline1 = Sequence.createLifeline(a);
		lifeline2 = Sequence.createLifeline(b);
		lifeline3 = Sequence.createLifeline(c);
	}
}
