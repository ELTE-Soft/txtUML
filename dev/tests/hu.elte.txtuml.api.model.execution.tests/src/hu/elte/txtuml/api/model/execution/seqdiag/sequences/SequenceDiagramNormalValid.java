package hu.elte.txtuml.api.model.execution.seqdiag.sequences;

import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.TestSig;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;
import hu.elte.txtuml.api.model.seqdiag.Action;
import hu.elte.txtuml.api.model.seqdiag.Actor;
import hu.elte.txtuml.api.model.seqdiag.ExecMode;
import hu.elte.txtuml.api.model.seqdiag.ExecutionMode;

@SequenceDiagramRelated
public class SequenceDiagramNormalValid extends SequenceBase {

	@Override
	@ExecutionMode(ExecMode.NORMAL)
	public void run() {
		Actor.send(new TestSig(), a);
		Action.send(new TestSig(), b, a);
		Action.send(new TestSig(), a, b);
	}

}
