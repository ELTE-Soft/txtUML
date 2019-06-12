package car.j.model;

import car.j.model.Car;
import car.j.model.Gearbox;
import car.j.model.associations.GearboxCar;
import car.j.model.datatypes.SpeedType;
import car.j.model.signals.ChangeGear;
import car.j.model.signals.ChangeSpeed;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.StateMachine;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;

public class Gearbox extends ModelClass {
	public class Init extends StateMachine.Initial {
	}

	public class N extends StateMachine.State {
	}

	public class R extends StateMachine.State {
	}

	public class First extends StateMachine.State {
	}

	public class Second extends StateMachine.State {
	}

	@From(Gearbox.Init.class)
	@To(Gearbox.N.class)
	public class Initialize extends StateMachine.Transition {
	}

	@From(Gearbox.N.class)
	@To(Gearbox.First.class)
	@Trigger(ChangeGear.class)
	public class NtoF extends StateMachine.Transition {
		@Override
		public void effect() {
			Car c = Gearbox.this.assoc(GearboxCar.c.class).one();
			Action.send(new ChangeSpeed(new SpeedType(1)), c);
		}

		@Override
		public boolean guard() {
			return (getTrigger(ChangeGear.class).gearType.gear == 1);
		}
	}

	@From(Gearbox.First.class)
	@To(Gearbox.Second.class)
	@Trigger(ChangeGear.class)
	public class FtoS extends StateMachine.Transition {
		@Override
		public void effect() {
			Car c = Gearbox.this.assoc(GearboxCar.c.class).one();
			Action.send(new ChangeSpeed(new SpeedType(1)), c);
		}

		@Override
		public boolean guard() {
			return (getTrigger(ChangeGear.class).gearType.gear == 2);
		}
	}

	@From(Gearbox.Second.class)
	@To(Gearbox.First.class)
	@Trigger(ChangeGear.class)
	public class StoF extends StateMachine.Transition {
		@Override
		public void effect() {
			Car c = Gearbox.this.assoc(GearboxCar.c.class).one();
			Action.send(new ChangeSpeed(new SpeedType((-1))), c);
		}

		@Override
		public boolean guard() {
			return (getTrigger(ChangeGear.class).gearType.gear == 1);
		}
	}

	@From(Gearbox.First.class)
	@To(Gearbox.N.class)
	@Trigger(ChangeGear.class)
	public class FtoN extends StateMachine.Transition {
		@Override
		public void effect() {
			Car c = Gearbox.this.assoc(GearboxCar.c.class).one();
			Action.send(new ChangeSpeed(new SpeedType(0)), c);
		}

		@Override
		public boolean guard() {
			return (getTrigger(ChangeGear.class).gearType.gear == 0);
		}
	}

	@From(Gearbox.N.class)
	@To(Gearbox.R.class)
	@Trigger(ChangeGear.class)
	public class NtoR extends StateMachine.Transition {
		@Override
		public void effect() {
			Car c = Gearbox.this.assoc(GearboxCar.c.class).one();
			Action.send(new ChangeSpeed(new SpeedType((-1))), c);
		}

		@Override
		public boolean guard() {
			return (getTrigger(ChangeGear.class).gearType.gear == (-1));
		}
	}

	@From(Gearbox.R.class)
	@To(Gearbox.N.class)
	@Trigger(ChangeGear.class)
	public class RtoN extends StateMachine.Transition {
		@Override
		public void effect() {
			Car c = Gearbox.this.assoc(GearboxCar.c.class).one();
			Action.send(new ChangeSpeed(new SpeedType(0)), c);
		}

		@Override
		public boolean guard() {
			return (getTrigger(ChangeGear.class).gearType.gear == 0);
		}
	}
}
