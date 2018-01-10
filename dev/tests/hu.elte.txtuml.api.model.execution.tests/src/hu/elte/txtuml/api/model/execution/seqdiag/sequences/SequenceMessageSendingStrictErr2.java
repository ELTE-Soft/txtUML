package hu.elte.txtuml.api.model.execution.seqdiag.sequences;

import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.TestSig;
import hu.elte.txtuml.api.model.seqdiag.API;
import hu.elte.txtuml.api.model.seqdiag.Action;
import hu.elte.txtuml.api.model.seqdiag.ExecMode;
import hu.elte.txtuml.api.model.seqdiag.ExecutionMode;

public class SequenceMessageSendingStrictErr2 extends SequenceBase {

	@Override
	@ExecutionMode(ExecMode.STRICT)
	public void run() {
		API.send(new TestSig(), a);
		Action.send(new TestSig(), b, a);
		Action.send(new TestSig(), c, b);
		Action.send(new TestSig(), a, b);
		Action.send(new TestSig(), b, c);
	}

}
