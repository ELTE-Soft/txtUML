package hu.elte.txtuml.api.model.execution.seqdiag.sequences;

import hu.elte.txtuml.api.model.seqdiag.API;
import hu.elte.txtuml.api.model.seqdiag.Action;
import hu.elte.txtuml.api.model.seqdiag.ExecMode;
import hu.elte.txtuml.api.model.seqdiag.ExecutionMode;
import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.TestSig;

public class SequenceMessageSendingStrictValid extends SequenceBase {

	@Override
	@ExecutionMode(ExecMode.STRICT)
	public void run() {
		API.send(new TestSig(), a);
		Action.send(a, new TestSig(), b);
		Action.send(b, new TestSig(), c);
		Action.send(c, new TestSig(), b);
		Action.send(b, new TestSig(), a);
	}

}
