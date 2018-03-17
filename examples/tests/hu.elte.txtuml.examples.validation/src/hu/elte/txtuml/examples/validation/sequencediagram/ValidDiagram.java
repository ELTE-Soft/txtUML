package hu.elte.txtuml.examples.validation.sequencediagram;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.seqdiag.Position;
import hu.elte.txtuml.api.model.seqdiag.Sequence;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import hu.elte.txtuml.examples.validation.sequencediagram.testmodel.A;
import hu.elte.txtuml.examples.validation.sequencediagram.testmodel.AToB;
import hu.elte.txtuml.examples.validation.sequencediagram.testmodel.B;
import hu.elte.txtuml.examples.validation.sequencediagram.testmodel.TestSig;

public class ValidDiagram extends SequenceDiagram {

	@Position(10)
	private A lifeline1;

	public B lifeline2;

	protected int someValue;

	@Override
	public void initialize() {
		lifeline1 = new A();
		lifeline2 = new B();
		someValue = 0;
		Action.link(AToB.ASide.class, lifeline1, AToB.BSide.class, lifeline2);
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

		Sequence.par(() -> {
			Sequence.send(lifeline1, new TestSig(), lifeline2);
		}, () -> {
			if (someValue < 10) {
				Sequence.send(lifeline1, new TestSig(), lifeline2);
			}
		});
	}

}

class Container extends SequenceDiagram {

	class Diagram1 extends SequenceDiagram {

		@Position(10)
		private A lifeline1;

		public B lifeline2;

		protected int someValue;

		@Override
		public void initialize() {
			lifeline1 = new A();
			lifeline2 = new B();
			someValue = 0;
			Action.link(AToB.ASide.class, lifeline1, AToB.BSide.class, lifeline2);
		}

		@Override
		public void run() {
			++someValue;
			if (someValue < 5) {
				Sequence.fromActor(new TestSig(), lifeline2);
			} else if (true && someValue == 10) {
				Sequence.send(lifeline1, new TestSig(), lifeline2);
				++someValue;
			}
		}
	}

	private Diagram1 diagr1;

	@Override
	public void initialize() {
		diagr1 = new Diagram1();
	}

	@Override
	public void run() {
		diagr1.run();
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