package hu.elte.txtuml.api.model.execution.seqdiag.sequences;

import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.TestSig;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;
import hu.elte.txtuml.api.model.seqdiag.ExecMode;
import hu.elte.txtuml.api.model.seqdiag.ExecutionMode;
import hu.elte.txtuml.api.model.seqdiag.Sequence;

@SequenceDiagramRelated
public class SequenceFragmentIF extends SequenceBase {

	public boolean condition;

	@Override
	@ExecutionMode(ExecMode.STRICT)
	public void run() {
		if (condition) {
			Sequence.fromActor(new TestSig(), a);
			Sequence.send(a, new TestSig(), b);
			Sequence.send(b, new TestSig(), c);
			Sequence.send(c, new TestSig(), b);
			Sequence.send(b, new TestSig(), a);
		} else {
			Sequence.fromActor(new TestSig(), b);
			Sequence.send(a, new TestSig(), b);
			Sequence.send(c, new TestSig(), b);
			Sequence.send(b, new TestSig(), a);
			Sequence.send(a, new TestSig(), b);
			Sequence.send(b, new TestSig(), c);
		}

	}

}
