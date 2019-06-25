package hu.elte.txtuml.examples.validation.sequencediagram;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.seqdiag.Lifeline;
import hu.elte.txtuml.api.model.seqdiag.Position;
import hu.elte.txtuml.api.model.seqdiag.Sequence;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import hu.elte.txtuml.examples.validation.sequencediagram.testmodel.A;
import hu.elte.txtuml.examples.validation.sequencediagram.testmodel.AToB;
import hu.elte.txtuml.examples.validation.sequencediagram.testmodel.B;
import hu.elte.txtuml.examples.validation.sequencediagram.testmodel.TestSig;

public class MissingSend extends SequenceDiagram {

	@Position(10)
	private Lifeline<A> lifeline1;

	private Lifeline<B> lifeline2;

	@Override
	public void initialize() {
		A a = new A();
		B b = new B();
		Action.link(AToB.ASide.class, a, AToB.BSide.class, b);
		lifeline1 = Sequence.createLifeline(a);
		lifeline2 = Sequence.createLifeline(b);
	}

	@Override
	public void run() {
		Sequence.fromActor(new TestSig(), lifeline1);
		if (true) {
			if (true) {
			} else {
				Sequence.assertSend(lifeline1, new TestSig(), lifeline2);
			}
		}
	}

}
