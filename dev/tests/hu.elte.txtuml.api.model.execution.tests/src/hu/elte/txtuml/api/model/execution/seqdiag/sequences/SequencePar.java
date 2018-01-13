package hu.elte.txtuml.api.model.execution.seqdiag.sequences;

import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.TestSig;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;
import hu.elte.txtuml.api.model.seqdiag.Sequence;

@SequenceDiagramRelated
public class SequencePar extends SequenceBase {

	@Override
	public void run() {
		Sequence.par(() -> {
			Sequence.send(a, new TestSig(), b);
		}, () -> {
			Sequence.send(b, new TestSig(), a);
		}, () -> {
			Sequence.send(b, new TestSig(), c);
			Sequence.send(c, new TestSig(), b);
		}, () -> {
			Sequence.fromActor(new TestSig(), a);
		});

	}

}
