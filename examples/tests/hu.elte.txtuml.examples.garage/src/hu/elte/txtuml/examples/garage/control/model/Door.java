package hu.elte.txtuml.examples.garage.control.model;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import hu.elte.txtuml.api.stdlib.timers.Timer;
import hu.elte.txtuml.examples.garage.control.glue.View;
import hu.elte.txtuml.examples.garage.control.model.associations.MotorMovesDoor;
import hu.elte.txtuml.examples.garage.control.model.signals.external.MotionSensorActivated;
import hu.elte.txtuml.examples.garage.control.model.signals.external.RemoteControlButtonPressed;
import hu.elte.txtuml.examples.garage.control.model.signals.internal.ChangeMotorMode;
import hu.elte.txtuml.examples.garage.control.model.signals.internal.DoorTimerExpired;
import hu.elte.txtuml.examples.garage.control.model.signals.internal.ReenableMotor;

public class Door extends ModelClass {
	Timer doorTimer;

	public class InitDoor extends Initial {
	}

	public class Enabled extends State {
		@Override
		public void entry() {
			Motor m = Door.this.assoc(MotorMovesDoor.movingMotor.class).one();
			Action.send(new ReenableMotor(), m);
		}
	}

	public class Disabled extends State {
		@Override
		public void entry() {
			View.getInstance().stopDoor();
		}
	}

	@From(InitDoor.class)
	@To(Enabled.class)
	public class TInitDoor extends Transition {
	}

	@From(Enabled.class)
	@To(Disabled.class)
	@Trigger(MotionSensorActivated.class)
	public class TDisable extends Transition {
		@Override
		public void effect() {
			doorTimer = Timer.start(Door.this, new DoorTimerExpired(), 2000);
		}
	}

	@From(Disabled.class)
	@To(Disabled.class)
	@Trigger(MotionSensorActivated.class)
	public class TKeepDisabled extends Transition {
		@Override
		public void effect() {
			doorTimer.reset(2000);
		}
	}

	@From(Disabled.class)
	@To(Enabled.class)
	@Trigger(DoorTimerExpired.class)
	public class TEnable extends Transition {
	}

	@From(Enabled.class)
	@To(Enabled.class)
	@Trigger(RemoteControlButtonPressed.class)
	public class TAcceptRemoteControl extends Transition {
		@Override
		public void effect() {
			Motor m = Door.this.assoc(MotorMovesDoor.movingMotor.class).one();
			Action.send(new ChangeMotorMode(), m);
		}
	}
}