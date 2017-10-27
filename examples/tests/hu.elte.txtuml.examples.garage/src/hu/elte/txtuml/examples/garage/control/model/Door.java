package hu.elte.txtuml.examples.garage.control.model;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import hu.elte.txtuml.api.stdlib.timers.Timer;
import hu.elte.txtuml.api.stdlib.world.SignalToWorld;
import hu.elte.txtuml.api.stdlib.world.World;
import hu.elte.txtuml.examples.garage.control.model.associations.DoorUsesTimer;
import hu.elte.txtuml.examples.garage.control.model.associations.MotorMovesDoor;
import hu.elte.txtuml.examples.garage.control.model.signals.external.in.MotionSensorActivated;
import hu.elte.txtuml.examples.garage.control.model.signals.external.in.RemoteControlButtonPressed;
import hu.elte.txtuml.examples.garage.control.model.signals.external.out.StopDoor;
import hu.elte.txtuml.examples.garage.control.model.signals.internal.ChangeMotorMode;
import hu.elte.txtuml.examples.garage.control.model.signals.internal.DoorTimerExpired;
import hu.elte.txtuml.examples.garage.control.model.signals.internal.ReenableMotor;

class S extends SignalToWorld {}

public class Door extends ModelClass {

	public class InitDoor extends Initial {
	}

	public class Enabled extends State {
		@Override
		public void entry() {
			Motor m = Door.this.assoc(MotorMovesDoor.movingMotor.class).selectAny();
			Action.send(new ReenableMotor(), m);
		}
	}

	public class Disabled extends State {
		@Override
		public void entry() {
			Action.send(new StopDoor(), World.get(View.id()));
		}
	}

	@From(InitDoor.class)
	@To(Enabled.class)
	public class TInitDoor extends Transition {
		@Override
		public void effect() {
			Action.send(new S(), World.get(View.id()));
		}
	}

	@From(Enabled.class)
	@To(Disabled.class)
	@Trigger(MotionSensorActivated.class)
	public class TDisable extends Transition {
		@Override
		public void effect() {
			if (!assoc(DoorUsesTimer.timer.class).isEmpty()) {
				Timer timer = assoc(DoorUsesTimer.timer.class).selectAny();
				Action.unlink(DoorUsesTimer.timer.class, timer, DoorUsesTimer.door.class, Door.this);				
			}
			Timer timer = Timer.start(new DoorTimerExpired(), Door.this, 2000);
			Action.link(DoorUsesTimer.timer.class, timer, DoorUsesTimer.door.class, Door.this);
		}
	}

	@From(Disabled.class)
	@To(Disabled.class)
	@Trigger(MotionSensorActivated.class)
	public class TKeepDisabled extends Transition {
		@Override
		public void effect() {
			assoc(DoorUsesTimer.timer.class).selectAny().reset(2000);
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
			Motor m = Door.this.assoc(MotorMovesDoor.movingMotor.class).selectAny();
			Action.send(new ChangeMotorMode(), m);
		}
	}
}