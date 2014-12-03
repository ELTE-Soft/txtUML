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
	class KeyPress extends Signal {
		ModelInt key;
		KeyPress(ModelInt k) {
			this.key = k;
		}
	}
	class StarPressed extends Signal {}
	class HashPressed extends Signal {}
	// Internal signals
	class DoorTimerExpired extends Signal {}
	class ReenableMotor extends Signal {}
	class ChangeMotorMode extends Signal {}
	class AlarmTimerExpired extends Signal {}

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
		class TStopAtBottom extends Transition {
			@Override public void effect() {
				Door d = Motor.this.assoc(MotorMovesDoor.movedDoor.class).selectOne();
				Alarm a = d.assoc(DoorSwitchesOnAlarm.SwitchedAlarm.class).selectOne();
				Action.send(a, new StarPressed());
			}
		}
		@From(MovingUp.class) @To(MovingUp.class) @Trigger(ReenableMotor.class)
		class TRestartUp extends Transition {}
		@From(MovingDown.class) @To(MovingDown.class) @Trigger(ReenableMotor.class)
		class TRestartDown extends Transition {}
	}
	
	class MotorMovesDoor extends Association {
		class movedDoor extends One<Door> {}
		class movingMotor extends One<Motor> {}
	}
	
	class Alarm extends ModelClass {
		ModelInt code = new ModelInt(8);
		Timer.Handle alarmTimer;
		ModelInt alarmDelay = new ModelInt(5000);
		ModelInt alarmDelayInterval = new ModelInt(50);
		ModelInt elapsed = new ModelInt(0);
		
		class InitAlarm extends InitialState {}
		@From(InitAlarm.class) @To(On.class)
		class TInitAlarm extends Transition {}

		class Off extends State {
			@Override public void entry() {
				Glue.getInstance().controlled.stopSiren();
				Glue.getInstance().controlled.alarmOff();
			}
		}
		class On extends State {
			@Override public void entry() {
				Glue.getInstance().controlled.alarmOn();
			}
		}
		class ExpectingCode extends State {
			@Override public void entry() {
				Glue.getInstance().controlled.codeExpected();
//				alarmTimer = Timer.start(Alarm.this, new AlarmTimerExpired(), alarmDelayInterval);
			}
		}
		class InAlarm extends State {
			@Override public void entry() {
				Glue.getInstance().controlled.startSiren();
			}
		}
		class ExpectingOldCode extends State {
			@Override public void entry() {
				Glue.getInstance().controlled.oldCodeExpected();
			}
		}
		class ExpectingNewCode extends State {
			@Override public void entry() {
				Glue.getInstance().controlled.newCodeExpected();
			}
		}
		
		@From(On.class) @To(ExpectingCode.class) @Trigger(AlarmSensorActivated.class)
		class TActivate extends Transition {}
		@From(On.class) @To(InAlarm.class) @Trigger(KeyPress.class)
		class TWrongKey1 extends Transition {
			@Override public ModelBool guard() {
				return ((KeyPress)getSignal()).key.isEqual(code).not();
			}
		}
		@From(On.class) @To(Off.class) @Trigger(KeyPress.class)
		class TCorrectKey1 extends Transition {
			@Override public ModelBool guard() {
				return ((KeyPress)getSignal()).key.isEqual(code);
			}
		}		
		@From(ExpectingCode.class) @To(InAlarm.class) @Trigger(KeyPress.class)
		class TWrongKey2 extends Transition {
			@Override public ModelBool guard() {
				return ((KeyPress)getSignal()).key.isEqual(code).not();
			}
		}
		@From(ExpectingCode.class) @To(Off.class) @Trigger(KeyPress.class)
		class TCorrectKey2 extends Transition {
			@Override public ModelBool guard() {
				return ((KeyPress)getSignal()).key.isEqual(code);
			}
		}		
		@From(InAlarm.class) @To(Off.class) @Trigger(KeyPress.class)
		class TCorrectKey3 extends Transition {
			@Override public ModelBool guard() {
				return ((KeyPress)getSignal()).key.isEqual(code);
			}
		}		
		@From(ExpectingOldCode.class) @To(ExpectingNewCode.class) @Trigger(KeyPress.class)
		class TCorrectKey4 extends Transition {
			@Override public ModelBool guard() {
				return ((KeyPress)getSignal()).key.isEqual(code);
			}
		}		
		@From(ExpectingOldCode.class) @To(Off.class) @Trigger(KeyPress.class)
		class TWrongKey4 extends Transition {
			@Override public ModelBool guard() {
				return ((KeyPress)getSignal()).key.isEqual(code).not();
			}
		}		
		@From(ExpectingNewCode.class) @To(Off.class) @Trigger(KeyPress.class)
		class TNewKey extends Transition {
			@Override public void effect() {
				code = ((KeyPress)getSignal()).key;
			}
		}		
		@From(Off.class) @To(On.class) @Trigger(StarPressed.class)
		class TSwitchOn extends Transition {}
		@From(Off.class) @To(ExpectingOldCode.class) @Trigger(HashPressed.class)
		class TChangeCode extends Transition {}
		@From(ExpectingCode.class) @To(InAlarm.class) @Trigger(AlarmTimerExpired.class)
		class TTimeOut extends Transition {
//			@Override public ModelBool guard() {
//				
//			}
		}
	}
	
	class DoorSwitchesOnAlarm extends Association {
		class SwitchingDoor extends One<Door> {}
		class SwitchedAlarm extends One<Alarm> {}
	}
}
