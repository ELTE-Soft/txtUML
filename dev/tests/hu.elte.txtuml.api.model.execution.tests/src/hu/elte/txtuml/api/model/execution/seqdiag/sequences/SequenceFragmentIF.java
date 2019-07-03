package hu.elte.txtuml.api.model.execution.seqdiag.sequences;

import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.A;
import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.B;
import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.C;
import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.TestSig;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;
import hu.elte.txtuml.api.model.seqdiag.ExecMode;
import hu.elte.txtuml.api.model.seqdiag.ExecutionMode;
import hu.elte.txtuml.api.model.seqdiag.Sequence;

@SequenceDiagramRelated
public class SequenceFragmentIF extends SequenceBase {

	public boolean condition;

	@Override
	@ExecutionMode(ExecMode.STRICT)
	public void run() {
		if (condition) {
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
		} else {
			Sequence.assertState(a, A.StateA.class);
			Sequence.assertState(b, B.StateA.class);
			Sequence.assertState(c, C.StateA.class);
			Sequence.fromActor(new TestSig(), b);
			Sequence.assertState(b, B.StateB.class);
			Sequence.assertSend(a, new TestSig(), b);
			Sequence.assertState(a, A.StateB.class);
			Sequence.assertSend(c, new TestSig(), b);
			Sequence.assertSend(b, new TestSig(), a);
			Sequence.assertSend(a, new TestSig(), b);
			Sequence.assertSend(b, new TestSig(), c);
		}

	}

}
