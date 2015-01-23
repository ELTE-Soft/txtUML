package txtuml.examples.garage.control.java;

import java.util.Timer;
import java.util.TimerTask;

import txtuml.examples.garage.interfaces.IControl;
import txtuml.examples.garage.interfaces.IControlled;

public class ControlJava implements IControl {
	static IControlled controlled;
	enum DoorSecurityState {Disabled, Enabled};
	static DoorSecurityState doorSecurityState = DoorSecurityState.Enabled;
	enum DoorState {HeadingUp, HeadingDown, Up, Down};
	static DoorState doorState = DoorState.HeadingUp;
	static Timer doorSecurityTimer = new Timer();
	enum AlarmState {Idle, Alarmed, CodeExpected, InAlarm, OldCodeExpected, NewCodeExpected};
	static AlarmState alarmState = AlarmState.Alarmed;
	static private int code = 8;
	static Timer alarmTimer = new Timer();
	static int alarmDelay = 5000;
	static int alarmDelayInterval = 50;

	public ControlJava(IControlled ctd) {
		controlled = ctd;
	}
	
	@Override
	public void remoteControlButtonPressed() {
		if(doorSecurityState == DoorSecurityState.Enabled) {
			switch(doorState) {
			case HeadingUp:
				doorState = DoorState.Up;
				controlled.startDoorUp();
				break;
			case HeadingDown:
				doorState = DoorState.Down;
				controlled.startDoorDown();
				break;
			case Up:
				doorState = DoorState.HeadingDown;
				controlled.stopDoor();
				break;
			case Down:
				doorState = DoorState.HeadingUp;
				controlled.stopDoor();
				break;
			}
		}
	}

	public void motionSensorActivated() {
		controlled.stopDoor();
		switch(doorState) {
		case Up:
		case Down:
			doorSecurityState = DoorSecurityState.Disabled;
			doorSecurityTimer.cancel();
			doorSecurityTimer = new Timer();
			doorSecurityTimer.schedule(new Enable(this), 2000);
			break;
		default:
		}
	}
	
	public void doorReachedTop() {
		controlled.stopDoor();
		doorState = DoorState.HeadingDown;
	}
	
	public void doorReachedBottom() {
		controlled.stopDoor();
		doorState = DoorState.HeadingUp;
		if(alarmState == AlarmState.Idle) {
			alarmState = AlarmState.Alarmed;
			controlled.alarmOn();
		}
	}

	public void keyPress(int nr) {
		if(alarmState == AlarmState.CodeExpected || alarmState == AlarmState.Alarmed) {
			alarmTimer.cancel();
			alarmTimer = new Timer();
			if(nr == code) {
				alarmState = AlarmState.Idle;
				controlled.alarmOff();
			} else {
				alarmState = AlarmState.InAlarm;
				controlled.startSiren();
				controlled.codeExpected();
			}
		} else if(alarmState == AlarmState.InAlarm) {
			if(nr == code) {
				alarmState = AlarmState.Idle;
				controlled.stopSiren();
				controlled.alarmOff();
			}
		} else if(alarmState == AlarmState.OldCodeExpected) {
			alarmTimer.cancel();
			alarmTimer = new Timer();
			if(nr == code) {
				alarmState = AlarmState.NewCodeExpected;
				alarmTimer.scheduleAtFixedRate(new CodeDelay(this), alarmDelayInterval, alarmDelayInterval);
			} else {
				alarmState = AlarmState.Idle;
				controlled.alarmOff();
			}			
		} else if(alarmState == AlarmState.NewCodeExpected) {
			alarmTimer.cancel();
			alarmTimer = new Timer();
			code = nr;
			alarmState = AlarmState.Idle;
			controlled.alarmOff();
		}
	}

	public void starPressed() {
		if(alarmState == AlarmState.Idle) {
			alarmState = AlarmState.Alarmed;
			controlled.alarmOn();
		}
	}
	
	public void hashPressed() {
		if(alarmState == AlarmState.Idle) {
			alarmState = AlarmState.OldCodeExpected;
			alarmTimer.scheduleAtFixedRate(new CodeDelay(this), alarmDelayInterval, alarmDelayInterval);			
		}
	}
	
	public void alarmSensorActivated() {
		if(alarmState == AlarmState.Alarmed) {
			alarmState = AlarmState.CodeExpected;
			alarmTimer.scheduleAtFixedRate(new CodeDelay(this), alarmDelayInterval, alarmDelayInterval);
		}
	}
	
	class Enable extends TimerTask {
		private ControlJava parent;
		
		Enable(ControlJava p) {
			parent = p;
		}
		
		public void run() {
			if(parent.doorSecurityState == DoorSecurityState.Disabled) {
				parent.doorSecurityState = DoorSecurityState.Enabled;
				switch(parent.doorState) {
				case Down:
					parent.controlled.startDoorDown();
					break;
				case Up:
					parent.controlled.startDoorUp();
					break;
				default:
				}
			}
		}
	}
	
	class CodeDelay extends TimerTask {
		private int elapsed = 0;
		private ControlJava parent;
		
		CodeDelay(ControlJava p) {
			parent = p;
		}
		
		@Override
		public void run() {
			elapsed += parent.alarmDelayInterval;
			elapsed = Math.min(elapsed,parent.alarmDelay);
			parent.controlled.progress(100*elapsed/parent.alarmDelay);
			if(alarmState == AlarmState.CodeExpected) {
				parent.controlled.codeExpected();
			} else if(alarmState == AlarmState.OldCodeExpected) {
				parent.controlled.oldCodeExpected();
			} else if(alarmState == AlarmState.NewCodeExpected) {
				parent.controlled.newCodeExpected();
			}
			if(elapsed >= parent.alarmDelay) {
				parent.alarmTimer.cancel();
				parent.alarmTimer = new Timer();
				if(parent.alarmState == AlarmState.CodeExpected) {
					parent.alarmState = AlarmState.InAlarm;
					parent.controlled.startSiren();
				} else if(parent.alarmState == AlarmState.OldCodeExpected
					   || parent.alarmState == AlarmState.NewCodeExpected) {
					parent.alarmState = AlarmState.Idle;
					parent.controlled.alarmOff();
				}
			}
		}	
	}
}
