package train.j.model;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import train.j.model.associations.GE;
import train.j.model.associations.GL;
import train.j.model.signals.Backward;
import train.j.model.signals.EngineOff;
import train.j.model.signals.EngineOn;
import train.j.model.signals.Forward;
import train.j.model.signals.LightOff;

public class Gearbox extends ModelClass {
	public class Init extends Initial {
	}

	public class Neutral extends State {
	}

	public class Forwards extends CompositeState {
		public class FInit extends Initial {
		}

		public class F1 extends State {
		}

		public class F2 extends State {
		}

		@From(FInit.class)
		@To(F1.class)
		class FInit_F1 extends Transition {
		}

		@From(F1.class)
		@To(F2.class)
		@Trigger(Forward.class)
		class F1_F2 extends Transition {
		}

		@From(F2.class)
		@To(F1.class)
		@Trigger(Forward.class)
		class F2_F1 extends Transition {
		}
	}

	public class Backwards extends CompositeState {
		public class BInit extends Initial {
		}

		public class B1 extends State {
		}

		public class B2 extends State {
		}

		@From(BInit.class)
		@To(B1.class)
		class BInit_B1 extends Transition {
		}

		@From(B1.class)
		@To(B2.class)
		@Trigger(Backward.class)
		class B1_B2 extends Transition {
		}

		@From(B2.class)
		@To(B1.class)
		@Trigger(Backward.class)
		class B2_B1 extends Transition {
		}
	}

	@From(Init.class)
	@To(Neutral.class)
	class Init_Neutral extends Transition {
	}

	@From(Neutral.class)
	@To(Forwards.class)
	@Trigger(Forward.class)
	class Neutral_Forwards extends Transition {
		@Override
		public void effect() {
			startEngineOp();
		}
	}

	@From(Neutral.class)
	@To(Backwards.class)
	@Trigger(Backward.class)
	class Neutral_Backwards extends Transition {
		@Override
		public void effect() {
			startEngineOp();
		}
	}

	@From(Forwards.class)
	@To(Neutral.class)
	@Trigger(Backward.class)
	class Forwards_Neutral extends Transition {
		@Override
		public void effect() {
			Engine e = Gearbox.this.assoc(GE.e.class).selectAny();
			Action.send(new EngineOff(), e);
			Lamp l = Gearbox.this.assoc(GL.l.class).selectAny();
			Action.send(new LightOff(), l);
		}
	}

	@From(Backwards.class)
	@To(Neutral.class)
	@Trigger(Forward.class)
	class Backwards_Neutral extends Forwards_Neutral {
		@Override
		public void effect() {
			Engine e = Gearbox.this.assoc(GE.e.class).selectAny();
			Action.send(new EngineOff(), e);
			Lamp l = Gearbox.this.assoc(GL.l.class).selectAny();
			Action.send(new LightOff(), l);
		}
	}

	void startEngineOp() {
		Engine e = Gearbox.this.assoc(GE.e.class).selectAny();
		Action.send(new EngineOn(), e);
	}
}