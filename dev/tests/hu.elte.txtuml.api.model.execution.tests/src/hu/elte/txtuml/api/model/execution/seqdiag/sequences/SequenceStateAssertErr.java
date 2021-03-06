package hu.elte.txtuml.api.model.execution.seqdiag.sequences;

import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.A;
import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.B;
import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.C;
import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.TestSig;
import hu.elte.txtuml.api.model.seqdiag.Sequence;

public class SequenceStateAssertErr extends SequenceBase {

	@Override
	public void run() {
		Sequence.assertState(a, A.StateA.class);
		Sequence.assertState(b, B.StateA.class);
		Sequence.assertState(c, C.StateB.class);
		Sequence.fromActor(new TestSig(), a);
		Sequence.assertState(a, A.StateA.class);
		Sequence.assertSend(a, new TestSig(), b);
		Sequence.assertState(b, B.StateB.class);
		Sequence.assertSend(b, new TestSig(), c);
		Sequence.assertState(c, C.StateA.class);
		Sequence.assertSend(c, new TestSig(), b);
		Sequence.assertState(b, B.StateA.class);
		Sequence.assertSend(b, new TestSig(), a);
		Sequence.assertState(a, A.StateA.class);
	}

}
