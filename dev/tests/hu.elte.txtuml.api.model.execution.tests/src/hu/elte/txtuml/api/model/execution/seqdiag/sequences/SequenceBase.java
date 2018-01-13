package hu.elte.txtuml.api.model.execution.seqdiag.sequences;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.A;
import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.AToB;
import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.B;
import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.BToC;
import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.C;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;
import hu.elte.txtuml.api.model.seqdiag.Position;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;

@SequenceDiagramRelated
public abstract class SequenceBase extends SequenceDiagram {

	@Position(1)
	public A a;
	@Position(3)
	public B b;
	@Position(2)
	public C c;

	@Override
	public void initialize() {
		a = Action.create(A.class);
		b = Action.create(B.class);

		Action.link(AToB.ASide.class, a, AToB.BSide.class, b);

		c = Action.create(C.class);

		Action.link(BToC.BSide.class, b, BToC.CSide.class, c);

		Action.start(a);
		Action.start(b);
		Action.start(c);
	}

}
