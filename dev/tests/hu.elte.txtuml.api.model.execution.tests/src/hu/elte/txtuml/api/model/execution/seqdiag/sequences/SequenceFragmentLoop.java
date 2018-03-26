package hu.elte.txtuml.api.model.execution.seqdiag.sequences;

import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.TestSig;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;
import hu.elte.txtuml.api.model.seqdiag.Sequence;

@SequenceDiagramRelated
public class SequenceFragmentLoop extends SequenceBase {

	@Override
	public void initialize() {
		super.initialize();
	}

	@Override
	public void run() {
		for (int i = 0; i < 1; ++i) {
			Sequence.fromActor(new TestSig(), a);
			Sequence.send(a, new TestSig(), b);
			Sequence.send(b, new TestSig(), c);
			Sequence.send(c, new TestSig(), b);
			Sequence.send(b, new TestSig(), a);
		}

	}

}
