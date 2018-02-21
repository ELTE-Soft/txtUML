package hu.elte.txtuml.api.model.execution.seqdiag.sequences;

import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.TestSig;
import hu.elte.txtuml.api.model.seqdiag.API;
import hu.elte.txtuml.api.model.seqdiag.Action;

public class SequenceFragmentLoop extends SequenceBase {

	@Override
	public void initialize() {
		super.initialize();
	}

	@Override
	public void run() {
		for (int i = 0; i < 1; ++i) {
			API.send(new TestSig(), a);
			Action.send(a, new TestSig(), b);
			Action.send(b, new TestSig(), c);
			Action.send(c, new TestSig(), b);
			Action.send(b, new TestSig(), a);
		}

	}

}
