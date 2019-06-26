package hu.elte.txtuml.seqdiag.tests.models.sequences;

import hu.elte.txtuml.api.model.seqdiag.ExecMode;
import hu.elte.txtuml.api.model.seqdiag.ExecutionMode;
import hu.elte.txtuml.api.model.seqdiag.Sequence;
import hu.elte.txtuml.seqdiag.tests.models.testmodel.TestSig;

public class SequenceMessaging extends BaseSequence {

	@Override
	@ExecutionMode(ExecMode.STRICT)
	public void run() {
		Sequence.fromActor(new TestSig(), lifeline1);
		Sequence.assertSend(lifeline1, new TestSig(), lifeline2);
		Sequence.assertSend(lifeline2, new TestSig(), lifeline3);
		Sequence.assertSend(lifeline3, new TestSig(), lifeline2);
		Sequence.assertSend(lifeline2, new TestSig(), lifeline1);
	}

}
