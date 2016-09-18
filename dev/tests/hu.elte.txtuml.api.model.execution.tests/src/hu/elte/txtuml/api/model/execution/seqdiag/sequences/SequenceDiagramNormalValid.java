package hu.elte.txtuml.api.model.execution.seqdiag.sequences;

import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.TestSig;
import hu.elte.txtuml.api.model.seqdiag.API;
import hu.elte.txtuml.api.model.seqdiag.Action;
import hu.elte.txtuml.api.model.seqdiag.ExecMode;
import hu.elte.txtuml.api.model.seqdiag.ExecutionMode;

public class SequenceDiagramNormalValid extends SequenceBase {

	@Override
	@ExecutionMode(ExecMode.NORMAL)
	public void run() {
		API.send(new TestSig(), a);
		Action.send(a, new TestSig(), b);
		Action.send(b, new TestSig(), a);
	}

}
