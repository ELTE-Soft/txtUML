package hu.elte.txtuml.examples.validation.sequencediagram;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.seqdiag.Position;
import hu.elte.txtuml.api.model.seqdiag.Sequence;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import hu.elte.txtuml.examples.validation.sequencediagram.testmodel.A;
import hu.elte.txtuml.examples.validation.sequencediagram.testmodel.AToB;
import hu.elte.txtuml.examples.validation.sequencediagram.testmodel.B;
import hu.elte.txtuml.examples.validation.sequencediagram.testmodel.TestSig;

public class ValidDiagramWithMethodInvocations extends SequenceDiagram {

	@Position(10)
	private A lifeline1;

	public B lifeline2;

	@Override
	public void initialize() {
		lifeline1 = new A();
		lifeline2 = new B();
		Action.link(AToB.ASide.class, lifeline1, AToB.BSide.class, lifeline2);
	}

	@Override
	public void run() {
		someMethod();
	}

	private void someMethod() {
		Sequence.fromActor(new TestSig(), lifeline2);
	}

}
