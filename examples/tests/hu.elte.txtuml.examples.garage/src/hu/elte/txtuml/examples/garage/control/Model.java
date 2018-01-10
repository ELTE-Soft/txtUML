package hu.elte.txtuml.examples.garage.control;

import hu.elte.txtuml.api.model.API;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.Execution;
import hu.elte.txtuml.examples.garage.control.model.Alarm;
import hu.elte.txtuml.examples.garage.control.model.Door;
import hu.elte.txtuml.examples.garage.control.model.Keyboard;
import hu.elte.txtuml.examples.garage.control.model.Motor;
import hu.elte.txtuml.examples.garage.control.model.associations.DoorSwitchesOnAlarm;
import hu.elte.txtuml.examples.garage.control.model.associations.KeyboardProvidesCode;
import hu.elte.txtuml.examples.garage.control.model.associations.MotorMovesDoor;
import hu.elte.txtuml.examples.garage.control.model.signals.external.in.AlarmSensorActivated;
import hu.elte.txtuml.examples.garage.control.model.signals.external.in.DoorReachedBottom;
import hu.elte.txtuml.examples.garage.control.model.signals.external.in.DoorReachedTop;
import hu.elte.txtuml.examples.garage.control.model.signals.external.in.HashPressed;
import hu.elte.txtuml.examples.garage.control.model.signals.external.in.KeyPress;
import hu.elte.txtuml.examples.garage.control.model.signals.external.in.MotionSensorActivated;
import hu.elte.txtuml.examples.garage.control.model.signals.external.in.RemoteControlButtonPressed;
import hu.elte.txtuml.examples.garage.control.model.signals.external.in.StarPressed;
import hu.elte.txtuml.examples.garage.interfaces.Control;

public class Model implements Control, Execution {
	private Door door;
	private Motor motor;
	private Alarm alarm;
	private Keyboard keyboard;

	@Override
	public void initialization() {
		door = Action.create(Door.class);
		motor = Action.create(Motor.class);
		alarm = Action.create(Alarm.class);
		keyboard = Action.create(Keyboard.class);

		// Initialize links and start object instances
		Action.link(MotorMovesDoor.movedDoor.class, door, MotorMovesDoor.movingMotor.class, motor);
		Action.link(DoorSwitchesOnAlarm.SwitchingDoor.class, door, DoorSwitchesOnAlarm.SwitchedAlarm.class, alarm);
		Action.link(KeyboardProvidesCode.Provider.class, keyboard, KeyboardProvidesCode.Receiver.class, alarm);
		Action.start(door);
		Action.start(motor);
		Action.start(alarm);
		Action.start(keyboard);
	}

	@Override
	public void remoteControlButtonPressed() {
		API.send(new RemoteControlButtonPressed(), door);
	}

	@Override
	public void motionSensorActivated() {
		API.send(new MotionSensorActivated(), door);
	}

	@Override
	public void alarmSensorActivated() {
		API.send(new AlarmSensorActivated(), alarm);
	}

	@Override
	public void doorReachedTop() {
		API.send(new DoorReachedTop(), motor);
	}

	@Override
	public void doorReachedBottom() {
		API.send(new DoorReachedBottom(), motor);
	}

	@Override
	public void keyPress(int nr) {
		API.send(new KeyPress(nr), keyboard);
	}

	@Override
	public void starPressed() {
		API.send(new StarPressed(), alarm);
	}

	@Override
	public void hashPressed() {
		API.send(new HashPressed(), alarm);
	}

}
