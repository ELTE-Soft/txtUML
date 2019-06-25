package hu.elte.txtuml.api.model.execution.seqdiag.sequences;

import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.A;
import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.B;
import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.C;
import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.TestSig;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;
import hu.elte.txtuml.api.model.seqdiag.Sequence;

@SequenceDiagramRelated
public class SequenceFragmentLoop extends SequenceBase {

	@Override
	public void initialize() {
		super.initialize();
	}

	@Override
	public void run() {
		for (int i = 0; i < 1; ++i) {
			Sequence.assertState(a, A.StateA.class);
			Sequence.assertState(b, B.StateA.class);
			Sequence.assertState(c, C.StateA.class);
			Sequence.fromActor(new TestSig(), a);
			Sequence.assertState(a, A.StateB.class);
			Sequence.assertSend(a, new TestSig(), b);
			Sequence.assertState(b, B.StateB.class);
			Sequence.assertSend(b, new TestSig(), c);
			Sequence.assertState(c, C.StateB.class);
			Sequence.assertSend(c, new TestSig(), b);
			Sequence.assertState(b, B.StateA.class);
			Sequence.assertSend(b, new TestSig(), a);
			Sequence.assertState(a, A.StateA.class);
		}

	}

}
