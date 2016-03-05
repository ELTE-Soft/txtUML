package hu.elte.txtuml.examples.performance_test.model;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;

public class A extends ModelClass {
	int remainingCycles;

	int numForward;

	public A(final int numCycles) {
		this.remainingCycles = numCycles;
		this.numForward = 0;
	}

	public class init extends Initial {
	}

	public class A1 extends State {
		@Override
		public void entry() {
			if (remainingCycles-- <= 0) {
				for (B b : assoc(AB.b.class)) {
					Action.unlink(AB.a.class, A.this, AB.b.class, b);
				}
				Action.delete(A.this);
			}
		}
	}

	public class A2 extends State {
	}

	public class A3 extends State {
	}

	public class A4 extends State {
	}

	public class A5 extends State {
	}

	private void createChild() {
		B b = new B();
		Action.link(AB.a.class, this, AB.b.class, b);
		Action.start(b);
	}

	void childTerminated() {
		Action.send(new Backward(), this);
	}

	@From(A.init.class)
	@To(A.A1.class)
	public class init_A1 extends Transition {
		@Override
		public void effect() {
			// Action.log("initial transition");
			Action.send(new Forward(++numForward), A.this);
		}
	}

	@From(A.A1.class)
	@To(A.A2.class)
	@Trigger(Forward.class)
	public class A1_A2 extends Transition {
		@Override
		public void effect() {
			// Action.log("A1->A2");
			Forward sig = getSignal();
			if (sig.nf % 4 == 0) {
				createChild();
			}
			if (!assoc(AB.b.class).isEmpty()) {
				Action.send(new Forward(0), assoc(AB.b.class).selectAny());
			}
			Action.send(new Forward(++numForward), A.this);
		}
	}

	@From(A.A2.class)
	@To(A.A3.class)
	@Trigger(Forward.class)
	public class A2_A3 extends Transition {
		@Override
		public void effect() {
			// Action.log("A2->A3");
			Forward sig = getSignal();
			if (sig.nf % 4 == 0) {
				createChild();
			}
			if (!assoc(AB.b.class).isEmpty()) {
				Action.send(new Forward(0), assoc(AB.b.class).selectAny());
			}
			Action.send(new Forward(++numForward), A.this);
		}
	}

	@From(A.A3.class)
	@To(A.A4.class)
	@Trigger(Forward.class)
	public class A3_A4 extends Transition {
		@Override
		public void effect() {
			// Action.log("A3->A4");
			Forward sig = getSignal();
			if (sig.nf % 4 == 0) {
				createChild();
			}
			if (!assoc(AB.b.class).isEmpty()) {
				Action.send(new Forward(0), assoc(AB.b.class).selectAny());
			}
			Action.send(new Forward(++numForward), A.this);
		}
	}

	@From(A.A4.class)
	@To(A.A1.class)
	@Trigger(Forward.class)
	public class A4_A1 extends Transition {
		@Override
		public void effect() {
			// Action.log("A4->A1");
			Forward sig = getSignal();
			if (sig.nf % 4 == 0) {
				createChild();
			}
			if (!assoc(AB.b.class).isEmpty()) {
				Action.send(new Forward(0), assoc(AB.b.class).selectAny());
			}
			Action.send(new Forward(++numForward), A.this);
		}
	}

	@From(A.A1.class)
	@To(A.A4.class)
	@Trigger(Backward.class)
	public class A1_A4 extends Transition {
	}

	@From(A.A2.class)
	@To(A.A1.class)
	@Trigger(Backward.class)
	public class A2_A1 extends Transition {
	}

	@From(A.A3.class)
	@To(A.A2.class)
	@Trigger(Backward.class)
	public class A3_A2 extends Transition {
	}

	@From(A.A4.class)
	@To(A.A3.class)
	@Trigger(Backward.class)
	public class A4_A3 extends Transition {
	}	
}