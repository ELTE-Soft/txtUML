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
			Sequence.send(a, new TestSig(), b);
			Sequence.assertState(b, B.StateB.class);
			Sequence.send(b, new TestSig(), c);
			Sequence.assertState(c, C.StateB.class);
			Sequence.send(c, new TestSig(), b);
			Sequence.assertState(b, B.StateA.class);
			Sequence.send(b, new TestSig(), a);
			Sequence.assertState(a, A.StateA.class);
		} else {
			Sequence.assertState(a, A.StateA.class);
			Sequence.assertState(b, B.StateA.class);
			Sequence.assertState(c, C.StateA.class);
			Sequence.fromActor(new TestSig(), b);
			Sequence.assertState(b, B.StateB.class);
			Sequence.send(a, new TestSig(), b);
			Sequence.assertState(a, A.StateB.class);
			Sequence.send(c, new TestSig(), b);
			Sequence.send(b, new TestSig(), a);
			Sequence.send(a, new TestSig(), b);
			Sequence.send(b, new TestSig(), c);
		}

	}

}
