package hu.elte.txtuml.export.plantuml.tests.sequences;

import hu.elte.txtuml.api.model.seqdiag.API;
import hu.elte.txtuml.api.model.seqdiag.Action;
import hu.elte.txtuml.api.model.seqdiag.ExecMode;
import hu.elte.txtuml.api.model.seqdiag.ExecutionMode;
import hu.elte.txtuml.export.plantuml.tests.testModel.TestSig;

public class SequenceMessaging extends BaseSequence {

	@Override
	@ExecutionMode(ExecMode.Strict)
	public void run() {
		API.send(new TestSig(), lifeline1);
		Action.send(lifeline1, new TestSig(), lifeline2);
		Action.send(lifeline2, new TestSig(), lifeline3);
		Action.send(lifeline3, new TestSig(), lifeline2);
		Action.send(lifeline2, new TestSig(), lifeline1);
	}

}
