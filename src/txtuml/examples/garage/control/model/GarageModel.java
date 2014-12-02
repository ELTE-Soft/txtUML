package txtuml.examples.garage.control.model;

import txtuml.api.*;
import txtuml.external.Timer;

public class GarageModel extends Model {
	// Signals from external origin
	class RemoteControlButtonPressed extends Signal {}
	class MotionSensorActivated extends Signal {}
	class AlarmSensorActivated extends Signal {}
	class DoorReachedTop extends Signal {}
	class DoorReachedBottom extends Signal {}
	class KeyPress extends Signal {}
	class StarPressed extends Signal {}
	class HashPressed extends Signal {}
	// Internal signals
	class DoorTimerExpired extends Signal {}
	class ReenableMotor extends Signal {}
	class ChangeMotorMode extends Signal {}

	class Door extends ModelClass {
		Timer.Handle doorTimer;
		class InitDoor extends InitialState {}
		class Enabled extends State {
			@Override public void entry() {
				Motor m = Door.this.assoc(MotorMovesDoor.movingMotor.class).selectOne();
				Action.send(m,new ReenableMotor());
			}
		}
		class Disabled extends State {		
			@Override public void entry() {
				Glue.getInstance().controlled.stopDoor();
			}
		}
		@From(InitDoor.class) @To(Enabled.class)
		class TInitDoor extends Transition {}
		@From(Enabled.class) @To(Disabled.class) @Trigger(MotionSensorActivated.class)
		class TDisable extends Transition {
			@Override public void effect() {
				doorTimer = Timer.start(Door.this, new DoorTimerExpired(), 2000);
			}
		}
		@From(Disabled.class) @To(Disabled.class) @Trigger(MotionSensorActivated.class)
		class TKeepDisabled extends Transition {
			@Override public void effect() {
				doorTimer.reset(2000);
			}			
		}
		@From(Disabled.class) @To(Enabled.class) @Trigger(DoorTimerExpired.class)
		class TEnable extends Transition {}
		@From(Enabled.class) @To(Enabled.class) @Trigger(RemoteControlButtonPressed.class)
		class TAcceptRemoteControl extends Transition {
			@Override public void effect() {
				Motor m = Door.this.assoc(MotorMovesDoor.movingMotor.class).selectOne();
				Action.send(m, new ChangeMotorMode());
			}
		}
	}
	
	class Motor extends ModelClass {
		class InitMotor extends InitialState {}
		@From(InitMotor.class) @To(HeadingUp.class)
		class TInitMotor extends Transition {			
		}
		class MovingUp extends State {
			@Override public void entry() {
				Glue.getInstance().controlled.startDoorUp();
			}
		}
		class MovingDown extends State {
			@Override public void entry() {
				Glue.getInstance().controlled.startDoorDown();
			}
		}
		class HeadingUp extends State {
			@Override public void entry() {
				Glue.getInstance().controlled.stopDoor();
			}
		}
		class HeadingDown extends State {
			@Override public void entry() {
				Glue.getInstance().controlled.stopDoor();
			}
		}
		@From(MovingUp.class) @To(HeadingDown.class) @Trigger(ChangeMotorMode.class)
		class TStopMovingUp extends Transition {}
		@From(MovingDown.class) @To(HeadingUp.class) @Trigger(ChangeMotorMode.class)
		class TStopMovingDown extends Transition {}
		@From(HeadingUp.class) @To(MovingUp.class) @Trigger(ChangeMotorMode.class)
		class TStartMovingUp extends Transition {}
		@From(HeadingDown.class) @To(MovingDown.class) @Trigger(ChangeMotorMode.class)
		class TStartMovingDown extends Transition {}
		@From(MovingUp.class) @To(HeadingDown.class) @Trigger(DoorReachedTop.class)
		class TStopAtTop extends Transition {}
		@From(MovingDown.class) @To(HeadingUp.class) @Trigger(DoorReachedBottom.class)
		class TStopAtBottom extends Transition {}
		@From(MovingUp.class) @To(MovingUp.class) @Trigger(ReenableMotor.class)
		class TRestartUp extends Transition {}
		@From(MovingDown.class) @To(MovingDown.class) @Trigger(ReenableMotor.class)
		class TRestartDown extends Transition {}
	}
	
	class MotorMovesDoor extends Association {
		class movedDoor extends One<Door> {}
		class movingMotor extends One<Motor> {}
	}
}