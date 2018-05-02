package hu.elte.txtuml.seqdiag.tests.models.sequences;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.seqdiag.Position;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import hu.elte.txtuml.seqdiag.tests.models.testmodel.A;
import hu.elte.txtuml.seqdiag.tests.models.testmodel.AToB;
import hu.elte.txtuml.seqdiag.tests.models.testmodel.B;
import hu.elte.txtuml.seqdiag.tests.models.testmodel.BToC;
import hu.elte.txtuml.seqdiag.tests.models.testmodel.C;

public abstract class BaseSequence extends SequenceDiagram {
	@Position(1)
	public A lifeline1;
	@Position(3)
	public B lifeline2;
	@Position(2)
	public C lifeline3;

	@Override
	public void initialize() {
		lifeline1 = new A();
		lifeline2 = new B();
		lifeline3 = new C();
		Action.link(AToB.ASide.class, lifeline1, AToB.BSide.class, lifeline2);
		Action.link(BToC.BSide.class, lifeline2, BToC.CSide.class, lifeline3);
	}
}
