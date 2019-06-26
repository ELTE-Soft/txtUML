package hu.elte.txtuml.seqdiag.tests.models.sequences;

import hu.elte.txtuml.api.model.seqdiag.Sequence;
import hu.elte.txtuml.seqdiag.tests.models.testmodel.TestSig;

public class ParFragment extends BaseSequence {

	@Override
	public void run() {
		Sequence.par(() -> {

		}, () -> {
			Sequence.fromActor(new TestSig(), lifeline1);
			Sequence.assertSend(lifeline1, new TestSig(), lifeline2);
		});
	}

}
