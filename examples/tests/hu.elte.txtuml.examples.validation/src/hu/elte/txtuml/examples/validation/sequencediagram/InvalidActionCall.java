package hu.elte.txtuml.examples.validation.sequencediagram;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.examples.validation.sequencediagram.testmodel.A;
import hu.elte.txtuml.examples.validation.sequencediagram.testmodel.TestSig;

public class InvalidActionCall extends BaseSequence {

	@Override
	public void run() {
		Sequence.fromActor(new TestSig(), lifeline2);
		Action.create(A.class);
	}

}
