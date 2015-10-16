package hu.elte.txtuml.examples.garage.control.model;

import hu.elte.txtuml.api.model.*;
import hu.elte.txtuml.api.stdlib.*;

public class GarageModel extends Model {
	// Signals from external origin
	class RemoteControlButtonPressed extends Signal {}
	class MotionSensorActivated extends Signal {}
	class AlarmSensorActivated extends Signal {}
	class DoorReachedTop extends Signal {}
	class DoorReachedBottom extends Signal {}
	class KeyPress extends Signal {
		int key;
		KeyPress(int k) {
			this.key = k;
		}
	}
	class StarPressed extends Signal {}
	class HashPressed extends Signal {}
	// Internal signals
	class DoorTimerExpired extends Signal {}
	class ReenableMotor extends Signal {}
	class ChangeMotorMode extends Signal {}
	class KeyboardTimerExpired extends Signal {}
	class WaitForCode extends Signal {}
	class KeyboardTimeout extends Signal {}

	class Door extends ModelClass {
		Timer.Handle doorTimer;

		class InitDoor extends Initial {}
		class Enabled extends State {
			@Override public void entry() {
				Motor m = Door.this.assoc(MotorMovesDoor.movingMotor.class).selectAny();
				Action.send(m,new ReenableMotor());
			}
		}
		class Disabled extends State {		
			@Override public void entry() {
				Glue.getInstance().stopDoor();
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
				Motor m = Door.this.assoc(MotorMovesDoor.movingMotor.class).selectAny();	
				Action.send(m, new ChangeMotorMode());
			}
		}
	}
	
	class Motor extends ModelClass {
		class InitMotor extends Initial {}
		@From(InitMotor.class) @To(HeadingUp.class)
		class TInitMotor extends Transition {			
		}
		class MovingUp extends State {
			@Override public void entry() {
				Glue.getInstance().startDoorUp();
			}
		}
		class MovingDown extends State {
			@Override public void entry() {
				Glue.getInstance().startDoorDown();
			}
		}
		class HeadingUp extends State {
			@Override public void entry() {
				Glue.getInstance().stopDoor();
			}
		}
		class HeadingDown extends State {
			@Override public void entry() {
				Glue.getInstance().stopDoor();
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
				Door d = Motor.this.assoc(MotorMovesDoor.movedDoor.class).selectAny();
				Alarm a = d.assoc(DoorSwitchesOnAlarm.SwitchedAlarm.class).selectAny();
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
		int code = 8;
		
		class InitAlarm extends Initial {}
		@From(InitAlarm.class) @To(On.class)
		class TInitAlarm extends Transition {}

		class Off extends State {
			@Override public void entry() {
				Glue.getInstance().stopSiren();
				Glue.getInstance().alarmOff();
			}
		}
		class On extends State {
			@Override public void entry() {
				Glue.getInstance().alarmOn();
			}
		}
		class ExpectingCode extends State {
			@Override public void entry() {
				Glue.getInstance().codeExpected();
				Keyboard kb = Alarm.this.assoc(KeyboardProvidesCode.Provider.class).selectAny();
				Action.send(kb, new WaitForCode());
			}
		}
		class InAlarm extends State {
			@Override public void entry() {
				Glue.getInstance().startSiren();
			}
		}
		class ExpectingOldCode extends State {
			@Override public void entry() {
				Glue.getInstance().oldCodeExpected();
				Keyboard kb = Alarm.this.assoc(KeyboardProvidesCode.Provider.class).selectAny();
				Action.send(kb, new WaitForCode());
			}
		}
		class ExpectingNewCode extends State {
			@Override public void entry() {
				Glue.getInstance().newCodeExpected();
				Keyboard kb = Alarm.this.assoc(KeyboardProvidesCode.Provider.class).selectAny();
				Action.send(kb, new WaitForCode());
			}
		}
		
		@From(On.class) @To(ExpectingCode.class) @Trigger(AlarmSensorActivated.class)
		class TActivate extends Transition {}
		@From(On.class) @To(InAlarm.class) @Trigger(KeyPress.class)
		class TWrongKey1 extends Transition {
			@Override public boolean guard() {
				return getSignal(KeyPress.class).key != code;
			}
		}
		@From(On.class) @To(Off.class) @Trigger(KeyPress.class)
		class TCorrectKey1 extends Transition {
			@Override public boolean guard() {
				return getSignal(KeyPress.class).key == code;
			}
		}		
		@From(ExpectingCode.class) @To(InAlarm.class) @Trigger(KeyPress.class)
		class TWrongKey2 extends Transition {
			@Override public boolean guard() {
				return getSignal(KeyPress.class).key != code;
			}
		}
		@From(ExpectingCode.class) @To(Off.class) @Trigger(KeyPress.class)
		class TCorrectKey2 extends Transition {
			@Override public boolean guard() {
				return getSignal(KeyPress.class).key == code;
			}
		}		
		@From(InAlarm.class) @To(Off.class) @Trigger(KeyPress.class)
		class TCorrectKey3 extends Transition {
			@Override public boolean guard() {
				return getSignal(KeyPress.class).key == code;
			}
		}		
		@From(ExpectingOldCode.class) @To(ExpectingNewCode.class) @Trigger(KeyPress.class)
		class TCorrectKey4 extends Transition {
			@Override public boolean guard() {
				return getSignal(KeyPress.class).key == code;
			}
		}		
		@From(ExpectingOldCode.class) @To(Off.class) @Trigger(KeyPress.class)
		class TWrongKey4 extends Transition {
			@Override public boolean guard() {
				return getSignal(KeyPress.class).key != code;
			}
		}		
		@From(ExpectingNewCode.class) @To(Off.class) @Trigger(KeyPress.class)
		class TNewKey extends Transition {
			@Override public void effect() {
				code = getSignal(KeyPress.class).key;
			}
		}		
		@From(Off.class) @To(On.class) @Trigger(StarPressed.class)
		class TSwitchOn extends Transition {}
		@From(Off.class) @To(ExpectingOldCode.class) @Trigger(HashPressed.class)
		class TChangeCode extends Transition {}
		@From(ExpectingCode.class) @To(InAlarm.class) @Trigger(KeyboardTimeout.class)
		class TNoCodeGiven extends Transition {}
		@From(ExpectingOldCode.class) @To(Off.class) @Trigger(KeyboardTimeout.class)
		class TNoOldCodeGiven extends Transition {}
		@From(ExpectingNewCode.class) @To(Off.class) @Trigger(KeyboardTimeout.class)
		class TNoNewCodeGiven extends Transition {}
	}
	
	class DoorSwitchesOnAlarm extends Association {
		class SwitchingDoor extends One<Door> {}
		class SwitchedAlarm extends One<Alarm> {}
	}
	
	class Keyboard extends ModelClass {
		Timer.Handle keyboardTimer;
		int keyboardTimerCount;
		int keyboardTimerMaxCount = 100;
		
		class InitKeyboard extends Initial {}
		@From(InitKeyboard.class) @To(Idle.class)
		class TInitKeyboard extends Transition {}
		class Idle extends State {
			@Override public void entry() {
				keyboardTimerCount = 0;
			}
		}
		class Waiting extends State {}
		
		@From(Idle.class) @To(Idle.class) @Trigger(KeyPress.class) 
		class TSpontaneousKeyPress extends Transition {
			@Override public void effect() {
				Alarm a = Keyboard.this.assoc(KeyboardProvidesCode.Receiver.class).selectAny();
				Action.send(a, getSignal(KeyPress.class));
			}
		}
		@From(Idle.class) @To(Waiting.class) @Trigger(WaitForCode.class) 
		class TWaitForCode extends Transition {
			@Override public void effect() {
				keyboardTimerCount += 0;
				keyboardTimer = Timer.start(Keyboard.this, new KeyboardTimerExpired(), 50);
			}
		}
		@From(Waiting.class) @To(Waiting.class) @Trigger(KeyboardTimerExpired.class)
		class TRefreshProgress extends Transition {
			@Override public boolean guard() {
				return keyboardTimerCount < keyboardTimerMaxCount;
			}
			@Override public void effect() {
				keyboardTimerCount += 1;
				Glue.getInstance().progress(keyboardTimerCount);
				keyboardTimer.reset(50);
			}
		}	
		@From(Waiting.class) @To(Idle.class) @Trigger(KeyPress.class)
		class TExpectedKeyPress extends Transition {
			@Override public void effect() {
				Alarm a = Keyboard.this.assoc(KeyboardProvidesCode.Receiver.class).selectAny();
				Action.send(a, getSignal(KeyPress.class));
			}
		}
		@From(Waiting.class) @To(Idle.class) @Trigger(KeyboardTimerExpired.class)
		class TTimeout extends Transition {
			@Override public boolean guard() {
				return keyboardTimerCount == keyboardTimerMaxCount;
			}
			@Override public void effect() {
				Alarm a = Keyboard.this.assoc(KeyboardProvidesCode.Receiver.class).selectAny();
				Action.send(a, new KeyboardTimeout());
			}
		}
	}
	
	class KeyboardProvidesCode extends Association {
		class Provider extends One<Keyboard> {}
		class Receiver extends One<Alarm> {}
	}
}
