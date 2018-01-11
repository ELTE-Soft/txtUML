package hu.elte.txtuml.api.model.execution.seqdiag.sequences;

import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.TestSig;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;
import hu.elte.txtuml.api.model.seqdiag.Action;
import hu.elte.txtuml.api.model.seqdiag.Actor;
import hu.elte.txtuml.api.model.seqdiag.ExecMode;
import hu.elte.txtuml.api.model.seqdiag.ExecutionMode;

@SequenceDiagramRelated
public class SequenceMessageSendingStrictErr1 extends SequenceBase {

	@Override
	@ExecutionMode(ExecMode.STRICT)
	public void run() {
		Action.send(new TestSig(), b, a);
		Actor.send(new TestSig(), a);
		Action.send(new TestSig(), c, b);
		Action.send(new TestSig(), b, c);
		Action.send(new TestSig(), a, b);
	}

}
