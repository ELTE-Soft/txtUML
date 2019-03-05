package hu.elte.txtuml.examples.validation.sequencediagram;

import hu.elte.txtuml.api.model.seqdiag.Position;
import hu.elte.txtuml.api.model.seqdiag.Sequence;
import hu.elte.txtuml.examples.validation.sequencediagram.testmodel.TestSig;

public class ValidDiagram extends BaseSequence {

	private int someValue;

	@Override
	public void initialize() {
		super.initialize();
		someValue = 0;
	}

	@Override
	public void run() {
		++someValue;
		if (someValue < 5) {
			Sequence.fromActor(new TestSig(), lifeline2);
		} else if (true && someValue == 10) {
			Sequence.send(lifeline1, new TestSig(), lifeline2);
			++someValue;
		} else {
			if (true) {
				if (false) {
					Sequence.send(lifeline1, new TestSig(), lifeline2);
				}
			}
		}
	}

}

class NotValidated {

	class NotModelClass {
	}

	@Position(-1)
	private NotModelClass lifeline1;

	public void initialize() {
	}

	public void run() {
	}

}
