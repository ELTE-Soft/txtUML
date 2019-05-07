package hu.elte.txtuml.api.model.execution.seqdiag.sequences;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.A;
import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.AToB;
import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.B;
import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.BToC;
import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.C;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;
import hu.elte.txtuml.api.model.seqdiag.Lifeline;
import hu.elte.txtuml.api.model.seqdiag.Position;
import hu.elte.txtuml.api.model.seqdiag.Sequence;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;

@SequenceDiagramRelated
public abstract class SequenceBase extends SequenceDiagram {

	@Position(1)
	public Lifeline<A> a;
	@Position(3)
	public Lifeline<B> b;
	@Position(2)
	public Lifeline<C> c;

	@Override
	public void initialize() {
		A obj1 = Action.create(A.class);
		B obj2 = Action.create(B.class);

		Action.link(AToB.ASide.class, obj1, AToB.BSide.class, obj2);

		C obj3 = Action.create(C.class);

		Action.link(BToC.BSide.class, obj2, BToC.CSide.class, obj3);

		Action.start(obj1);
		Action.start(obj2);
		Action.start(obj3);

		a = Sequence.createLifeline(obj1);
		b = Sequence.createLifeline(obj2);
		c = Sequence.createLifeline(obj3);
	}

}
