package hu.elte.txtuml.api.model.execution.seqdiag.sequences;

import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.TestSig;
import hu.elte.txtuml.api.model.seqdiag.API;
import hu.elte.txtuml.api.model.seqdiag.Action;
import hu.elte.txtuml.api.model.seqdiag.ExecMode;
import hu.elte.txtuml.api.model.seqdiag.ExecutionMode;

public class SequenceFragmentIF extends SequenceBase {

	public boolean condition;

	@Override
	@ExecutionMode(ExecMode.STRICT)
	public void run() {
		if (condition) {
			API.send(new TestSig(), a);
			Action.send(a, new TestSig(), b);
			Action.send(b, new TestSig(), c);
			Action.send(c, new TestSig(), b);
			Action.send(b, new TestSig(), a);
		} else {
			API.send(new TestSig(), b);
			Action.send(a, new TestSig(), b);
			Action.send(c, new TestSig(), b);
			Action.send(b, new TestSig(), a);
			Action.send(a, new TestSig(), b);
			Action.send(b, new TestSig(), c);
		}

	}

}
