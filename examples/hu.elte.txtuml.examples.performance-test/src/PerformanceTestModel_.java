import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.ModelExecutor;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.StateMachine;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;

import org.eclipse.uml2.uml.Usage;

@SuppressWarnings("all")
public class PerformanceTestModel_ extends Model {

	public class Test extends ModelClass {
		public void test() {
			A a = Action.create(PerformanceTestModel_.A.class, 100000);
			Action.start(a);
		}
	}

	public class A extends ModelClass {
		int remainingCycles;

		int numForward;

		public A(final int numCycles) {
			this.remainingCycles = numCycles;
			this.numForward = 0;
		}

		public class init extends StateMachine.Initial {
		}

		public class A1 extends StateMachine.State {
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

		public class A2 extends StateMachine.State {
		}

		public class A3 extends StateMachine.State {
		}

		public class A4 extends StateMachine.State {
		}

		public class A5 extends StateMachine.State {
		}

		private void createChild() {
			B b = new B();
			Action.link(AB.a.class, this, AB.b.class, b);
			Action.start(b);
		}

		private void childTerminated() {
			Action.send(this, new Backward());
		}

		@From(PerformanceTestModel_.A.init.class)
		@To(PerformanceTestModel_.A.A1.class)
		public class init_A1 extends StateMachine.Transition {
			@Override
			public void effect() {
				// Action.log("initial transition");
				Action.send(A.this, new Forward(++numForward));
			}
		}

		@From(PerformanceTestModel_.A.A1.class)
		@To(PerformanceTestModel_.A.A2.class)
		@Trigger(PerformanceTestModel_.Forward.class)
		public class A1_A2 extends StateMachine.Transition {
			@Override
			public void effect() {
//				Action.log("A1->A2");
				Forward sig = getSignal(Forward.class);
				if (sig.nf % 4 == 0) {
					createChild();
				}
				if (!assoc(AB.b.class).isEmpty()) {
					Action.send(assoc(AB.b.class).selectAny(), new Forward(0));
				}
				Action.send(A.this, new Forward(++numForward));
			}
		}

		@From(PerformanceTestModel_.A.A2.class)
		@To(PerformanceTestModel_.A.A3.class)
		@Trigger(PerformanceTestModel_.Forward.class)
		public class A2_A3 extends StateMachine.Transition {
			@Override
			public void effect() {
				// Action.log("A2->A3");
				Forward sig = getSignal(Forward.class);
				if (sig.nf % 4 == 0) {
					createChild();
				}
				if (!assoc(AB.b.class).isEmpty()) {
					Action.send(assoc(AB.b.class).selectAny(), new Forward(0));
				}
				Action.send(A.this, new Forward(++numForward));
			}
		}

		@From(PerformanceTestModel_.A.A3.class)
		@To(PerformanceTestModel_.A.A4.class)
		@Trigger(PerformanceTestModel_.Forward.class)
		public class A3_A4 extends StateMachine.Transition {
			@Override
			public void effect() {
				// Action.log("A3->A4");
				Forward sig = getSignal(Forward.class);
				if (sig.nf % 4 == 0) {
					createChild();
				}
				if (!assoc(AB.b.class).isEmpty()) {
					Action.send(assoc(AB.b.class).selectAny(), new Forward(0));
				}
				Action.send(A.this, new Forward(++numForward));
			}
		}

		@From(PerformanceTestModel_.A.A4.class)
		@To(PerformanceTestModel_.A.A1.class)
		@Trigger(PerformanceTestModel_.Forward.class)
		public class A4_A1 extends StateMachine.Transition {
			@Override
			public void effect() {
				// Action.log("A4->A1");
				Forward sig = getSignal(Forward.class);
				if (sig.nf % 4 == 0) {
					createChild();
				}
				if (!assoc(AB.b.class).isEmpty()) {
					Action.send(assoc(AB.b.class).selectAny(), new Forward(0));
				}
				Action.send(A.this, new Forward(++numForward));
			}
		}

		@From(PerformanceTestModel_.A.A1.class)
		@To(PerformanceTestModel_.A.A4.class)
		@Trigger(PerformanceTestModel_.Backward.class)
		public class A1_A4 extends StateMachine.Transition {
		}

		@From(PerformanceTestModel_.A.A2.class)
		@To(PerformanceTestModel_.A.A1.class)
		@Trigger(PerformanceTestModel_.Backward.class)
		public class A2_A1 extends StateMachine.Transition {
		}

		@From(PerformanceTestModel_.A.A3.class)
		@To(PerformanceTestModel_.A.A2.class)
		@Trigger(PerformanceTestModel_.Backward.class)
		public class A3_A2 extends StateMachine.Transition {
		}

		@From(PerformanceTestModel_.A.A4.class)
		@To(PerformanceTestModel_.A.A3.class)
		@Trigger(PerformanceTestModel_.Backward.class)
		public class A4_A3 extends StateMachine.Transition {
		}
	}

	public class B extends ModelClass {
		public class init extends StateMachine.Initial {
		}

		public class B1 extends StateMachine.State {
		}

		public class B2 extends StateMachine.State {
		}

		public class B3 extends StateMachine.State {
		}

		public class B4 extends StateMachine.State {
		}

		public class finalstate extends StateMachine.State {
			@Override
			public void entry() {
				A a = assoc(AB.a.class).selectAny();
				if (a != null) {
					Action.unlink(AB.a.class, a, AB.b.class, B.this);
					a.childTerminated();
				}
				Action.delete(B.this);
			}
		}

		@From(PerformanceTestModel_.B.init.class)
		@To(PerformanceTestModel_.B.B1.class)
		public class init_B1 extends StateMachine.Transition {
		}

		@From(PerformanceTestModel_.B.B1.class)
		@To(PerformanceTestModel_.B.B2.class)
		@Trigger(PerformanceTestModel_.Forward.class)
		public class B1_B2 extends StateMachine.Transition {
		}

		@From(PerformanceTestModel_.B.B2.class)
		@To(PerformanceTestModel_.B.B3.class)
		@Trigger(PerformanceTestModel_.Forward.class)
		public class B2_B3 extends StateMachine.Transition {
		}

		@From(PerformanceTestModel_.B.B3.class)
		@To(PerformanceTestModel_.B.B4.class)
		@Trigger(PerformanceTestModel_.Forward.class)
		public class B3_B4 extends StateMachine.Transition {
		}

		@From(PerformanceTestModel_.B.B4.class)
		@To(PerformanceTestModel_.B.finalstate.class)
		@Trigger(PerformanceTestModel_.Forward.class)
		public class B4_finalState extends StateMachine.Transition {
		}
	}

	public class AB extends Association {
		public class a extends Association.MaybeOne<PerformanceTestModel_.A> {
		}

		public class b extends Association.Many<PerformanceTestModel_.B> {
		}
	}

	public static class Forward extends Signal {
		int nf;

		public Forward(final int nf) {
			this.nf = nf;
		}
	}

	public static class Backward extends Signal {
	}
}
