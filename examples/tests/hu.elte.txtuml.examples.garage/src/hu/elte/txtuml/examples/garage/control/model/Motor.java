package hu.elte.txtuml.examples.garage.control.model;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import hu.elte.txtuml.examples.garage.control.glue.Glue;
import hu.elte.txtuml.examples.garage.control.model.associations.DoorSwitchesOnAlarm;
import hu.elte.txtuml.examples.garage.control.model.associations.MotorMovesDoor;
import hu.elte.txtuml.examples.garage.control.model.signals.external.DoorReachedBottom;
import hu.elte.txtuml.examples.garage.control.model.signals.external.DoorReachedTop;
import hu.elte.txtuml.examples.garage.control.model.signals.external.StarPressed;
import hu.elte.txtuml.examples.garage.control.model.signals.internal.ChangeMotorMode;
import hu.elte.txtuml.examples.garage.control.model.signals.internal.ReenableMotor;

public class Motor extends ModelClass {
	public class InitMotor extends Initial {
	}

	@From(InitMotor.class)
	@To(HeadingUp.class)
	public class TInitMotor extends Transition {
	}

	public class MovingUp extends State {
		@Override
		public void entry() {
			Glue.getInstance().startDoorUp();
		}
	}

	public class MovingDown extends State {
		@Override
		public void entry() {
			Glue.getInstance().startDoorDown();
		}
	}

	public class HeadingUp extends State {
		@Override
		public void entry() {
			Glue.getInstance().stopDoor();
		}
	}

	public class HeadingDown extends State {
		@Override
		public void entry() {
			Glue.getInstance().stopDoor();
		}
	}

	@From(MovingUp.class)
	@To(HeadingDown.class)
	@Trigger(ChangeMotorMode.class)
	public class TStopMovingUp extends Transition {
	}

	@From(MovingDown.class)
	@To(HeadingUp.class)
	@Trigger(ChangeMotorMode.class)
	public class TStopMovingDown extends Transition {
	}

	@From(HeadingUp.class)
	@To(MovingUp.class)
	@Trigger(ChangeMotorMode.class)
	public class TStartMovingUp extends Transition {
	}

	@From(HeadingDown.class)
	@To(MovingDown.class)
	@Trigger(ChangeMotorMode.class)
	public class TStartMovingDown extends Transition {
	}

	@From(MovingUp.class)
	@To(HeadingDown.class)
	@Trigger(DoorReachedTop.class)
	public class TStopAtTop extends Transition {
	}

	@From(MovingDown.class)
	@To(HeadingUp.class)
	@Trigger(DoorReachedBottom.class)
	public class TStopAtBottom extends Transition {
		@Override
		public void effect() {
			Door d = Motor.this.assoc(MotorMovesDoor.movedDoor.class).selectAny();
			Alarm a = d.assoc(DoorSwitchesOnAlarm.SwitchedAlarm.class).selectAny();
			Action.send(a, new StarPressed());
		}
	}

	@From(MovingUp.class)
	@To(MovingUp.class)
	@Trigger(ReenableMotor.class)
	public class TRestartUp extends Transition {
	}

	@From(MovingDown.class)
	@To(MovingDown.class)
	@Trigger(ReenableMotor.class)
	public class TRestartDown extends Transition {
	}
}