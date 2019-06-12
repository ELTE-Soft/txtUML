package hu.elte.txtuml.api.model.execution.seqdiag.sequences;

import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.A;
import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.B;
import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.C;
import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.TestSig;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;
import hu.elte.txtuml.api.model.seqdiag.Sequence;

@SequenceDiagramRelated
public class SequenceParErr extends SequenceBase {

	@Override
	public void run() {
		Sequence.par(() -> {
			Sequence.assertSend(a, new TestSig(), b);
		}, () -> {

		}, () -> {
			Sequence.assertSend(c, new TestSig(), b);
			Sequence.assertSend(c, new TestSig(), b);
		}, () -> {
			Sequence.assertSend(b, new TestSig(), c);
			Sequence.assertState(b, B.StateB.class);
			Sequence.assertState(c, C.StateB.class);
			Sequence.assertSend(b, new TestSig(), a);
			Sequence.assertSend(c, new TestSig(), b);
			Sequence.assertState(a, A.StateA.class);
			Sequence.assertState(b, B.StateA.class);
		}, () -> {
			Sequence.fromActor(new TestSig(), a);
			Sequence.assertState(a, A.StateB.class);
		});
		Sequence.assertSend(c, new TestSig(), b);
	}

}
