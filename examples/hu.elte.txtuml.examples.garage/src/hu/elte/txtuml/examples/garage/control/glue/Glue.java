package hu.elte.txtuml.examples.garage.control.glue;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.external.ExternalClass;
import hu.elte.txtuml.examples.garage.control.model.Alarm;
import hu.elte.txtuml.examples.garage.control.model.Door;
import hu.elte.txtuml.examples.garage.control.model.Keyboard;
import hu.elte.txtuml.examples.garage.control.model.Motor;
import hu.elte.txtuml.examples.garage.control.model.associations.DoorSwitchesOnAlarm;
import hu.elte.txtuml.examples.garage.control.model.associations.KeyboardProvidesCode;
import hu.elte.txtuml.examples.garage.control.model.associations.MotorMovesDoor;
import hu.elte.txtuml.examples.garage.control.model.signals.external.AlarmSensorActivated;
import hu.elte.txtuml.examples.garage.control.model.signals.external.DoorReachedBottom;
import hu.elte.txtuml.examples.garage.control.model.signals.external.DoorReachedTop;
import hu.elte.txtuml.examples.garage.control.model.signals.external.HashPressed;
import hu.elte.txtuml.examples.garage.control.model.signals.external.KeyPress;
import hu.elte.txtuml.examples.garage.control.model.signals.external.MotionSensorActivated;
import hu.elte.txtuml.examples.garage.control.model.signals.external.RemoteControlButtonPressed;
import hu.elte.txtuml.examples.garage.control.model.signals.external.StarPressed;
import hu.elte.txtuml.examples.garage.interfaces.IControl;
import hu.elte.txtuml.examples.garage.interfaces.IControlled;

// This class is the glue code between the UI and the control model
public class Glue implements ExternalClass, IControl, IControlled {
	// Model instantiation
	Door door = Action.create(Door.class);
	Motor motor = Action.create(Motor.class);;
	Alarm alarm = Action.create(Alarm.class);;
	Keyboard keyboard = Action.create(Keyboard.class);;

	// Linkage to the UI
	IControlled controlled;

	public void setControlled(IControlled ctd) {
		controlled = ctd;
	}

	// Singleton pattern
	static Glue instance = null;

	private Glue() {
		// Initialize links and start object instances
		Action.link(MotorMovesDoor.movedDoor.class, door,
				MotorMovesDoor.movingMotor.class, motor);
		Action.link(DoorSwitchesOnAlarm.SwitchingDoor.class, door,
				DoorSwitchesOnAlarm.SwitchedAlarm.class, alarm);
		Action.link(KeyboardProvidesCode.Provider.class, keyboard,
				KeyboardProvidesCode.Receiver.class, alarm);
		Action.start(door);
		Action.start(motor);
		Action.start(alarm);
		Action.start(keyboard);
	}

	public static synchronized Glue getInstance() {
		if (instance == null) {
			instance = new Glue();
		}
		return instance;
	}

	@Override
	public void progress(int percent) {
		controlled.progress(percent);
	}

	@Override
	public void stopDoor() {
		controlled.stopDoor();
	}

	@Override
	public void startDoorUp() {
		controlled.startDoorUp();
	}

	@Override
	public void startDoorDown() {
		controlled.startDoorDown();
	}

	@Override
	public void startSiren() {
		controlled.startSiren();
	}

	@Override
	public void stopSiren() {
		controlled.stopSiren();
	}

	@Override
	public void codeExpected() {
		controlled.codeExpected();
	}

	@Override
	public void oldCodeExpected() {
		controlled.oldCodeExpected();
	}

	@Override
	public void newCodeExpected() {
		controlled.newCodeExpected();
	}

	@Override
	public void alarmOff() {
		controlled.alarmOff();
	}

	@Override
	public void alarmOn() {
		controlled.alarmOn();
	}

	// IControl implementation
	@Override
	public void remoteControlButtonPressed() {
		Action.send(door, new RemoteControlButtonPressed());
	}

	@Override
	public void motionSensorActivated() {
		Action.send(door, new MotionSensorActivated());
	}

	@Override
	public void alarmSensorActivated() {
		Action.send(alarm, new AlarmSensorActivated());
	}

	@Override
	public void doorReachedTop() {
		Action.send(motor, new DoorReachedTop());
	}

	@Override
	public void doorReachedBottom() {
		Action.send(motor, new DoorReachedBottom());
	}

	@Override
	public void keyPress(int nr) {
		Action.send(keyboard, new KeyPress(nr));
	}

	@Override
	public void starPressed() {
		Action.send(alarm, new StarPressed());
	}

	@Override
	public void hashPressed() {
		Action.send(alarm, new HashPressed());
	}

}
