package hu.elte.txtuml.api.model.execution.seqdiag.sequences;

import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.TestSig;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;
import hu.elte.txtuml.api.model.seqdiag.ExecMode;
import hu.elte.txtuml.api.model.seqdiag.ExecutionMode;
import hu.elte.txtuml.api.model.seqdiag.Sequence;

@SequenceDiagramRelated
public class SequenceMessageSendingStrictValid extends SequenceBase {

	@Override
	@ExecutionMode(ExecMode.STRICT)
	public void run() {
		Sequence.fromActor(new TestSig(), a);
		Sequence.assertSend(a, new TestSig(), b);
		Sequence.assertSend(b, new TestSig(), c);
		Sequence.assertSend(c, new TestSig(), b);
		Sequence.assertSend(b, new TestSig(), a);
	}

}
