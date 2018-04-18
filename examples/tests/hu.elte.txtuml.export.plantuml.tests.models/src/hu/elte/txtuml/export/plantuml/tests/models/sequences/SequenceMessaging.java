package hu.elte.txtuml.export.plantuml.tests.models.sequences;

import hu.elte.txtuml.api.model.seqdiag.ExecMode;
import hu.elte.txtuml.api.model.seqdiag.ExecutionMode;
import hu.elte.txtuml.api.model.seqdiag.Sequence;
import hu.elte.txtuml.export.plantuml.tests.models.testmodel.TestSig;

public class SequenceMessaging extends BaseSequence {

	@Override
	@ExecutionMode(ExecMode.STRICT)
	public void run() {
		Sequence.fromActor(new TestSig(), lifeline1);
		Sequence.send(lifeline1, new TestSig(), lifeline2);
		Sequence.send(lifeline2, new TestSig(), lifeline3);
		Sequence.send(lifeline3, new TestSig(), lifeline2);
		Sequence.send(lifeline2, new TestSig(), lifeline1);
	}

}
