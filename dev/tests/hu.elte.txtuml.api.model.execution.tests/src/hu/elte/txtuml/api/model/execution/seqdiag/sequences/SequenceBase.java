package hu.elte.txtuml.api.model.execution.seqdiag.sequences;

import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.A;
import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.AToB;
import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.B;
import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.BToC;
import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.C;
import hu.elte.txtuml.api.model.seqdiag.Action;
import hu.elte.txtuml.api.model.seqdiag.Interaction;
import hu.elte.txtuml.api.model.seqdiag.Position;

public abstract class SequenceBase extends Interaction {

	@Position(1)
	public A a;
	@Position(3)
	public B b;
	@Position(2)
	public C c;

	@Override
	public void initialize() {
		a = new A();
		b = new B();

		Action.link(AToB.ASide.class, a, AToB.BSide.class, b);

		c = new C();

		Action.link(BToC.BSide.class, b, BToC.CSide.class, c);

		Action.start(a);
		Action.start(b);
		Action.start(c);
	}

}
