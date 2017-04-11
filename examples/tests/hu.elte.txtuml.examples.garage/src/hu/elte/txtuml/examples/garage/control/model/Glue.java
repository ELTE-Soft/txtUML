package hu.elte.txtuml.examples.garage.control.model;

import hu.elte.txtuml.api.model.API;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.External;
import hu.elte.txtuml.api.model.ExternalBody;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.execution.ModelExecutor;
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
import hu.elte.txtuml.examples.garage.interfaces.Control;
import hu.elte.txtuml.examples.garage.interfaces.Controlled;

/**
 *  This class is the glue code between the UI and the control model
 */
public class Glue extends ModelClass implements @External Control, @External Controlled {

	// Model instantiation
	@External
	Door door;
	@External
	Motor motor;
	@External
	Alarm alarm;
	@External
	Keyboard keyboard;

	// Linkage to the UI
	@External
	Controlled controlled;

	@External
	public void setControlled(Controlled ctd) {
		controlled = ctd;
	}

	// Singleton pattern
	@External
	private static Glue instance = null;

	private Glue() {
		ModelExecutor.create().start(() -> {
			try {
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
			} catch (Throwable t) {
				t.printStackTrace();
			}
		});
	}

	public static Glue getInstance() {
		synchronized (Glue.class) {
			if (instance == null) {
				instance = new Glue();
			}
			return instance;
		}
	}

	@Override
	@ExternalBody
	public void progress(int percent) {
		controlled.progress(percent);
	}

	@Override
	@ExternalBody
	public void stopDoor() {
		controlled.stopDoor();
	}

	@Override
	@ExternalBody
	public void startDoorUp() {
		controlled.startDoorUp();
	}

	@Override
	@ExternalBody
	public void startDoorDown() {
		controlled.startDoorDown();
	}

	@Override
	@ExternalBody
	public void startSiren() {
		controlled.startSiren();
	}

	@Override
	@ExternalBody
	public void stopSiren() {
		controlled.stopSiren();
	}

	@Override
	@ExternalBody
	public void codeExpected() {
		controlled.codeExpected();
	}

	@Override
	@ExternalBody
	public void oldCodeExpected() {
		controlled.oldCodeExpected();
	}

	@Override
	@ExternalBody
	public void newCodeExpected() {
		controlled.newCodeExpected();
	}

	@Override
	@ExternalBody
	public void alarmOff() {
		controlled.alarmOff();
	}

	@Override
	@ExternalBody
	public void alarmOn() {
		controlled.alarmOn();
	}

	// IControl implementation
	@Override
	@External
	public void remoteControlButtonPressed() {
		API.send(new RemoteControlButtonPressed(), door);
	}

	@Override
	@External
	public void motionSensorActivated() {
		API.send(new MotionSensorActivated(), door);
	}

	@Override
	@External
	public void alarmSensorActivated() {
		API.send(new AlarmSensorActivated(), alarm);
	}

	@Override
	@External
	public void doorReachedTop() {
		API.send(new DoorReachedTop(), motor);
	}

	@Override
	@External
	public void doorReachedBottom() {
		API.send(new DoorReachedBottom(), motor);
	}

	@Override
	@External
	public void keyPress(int nr) {
		API.send(new KeyPress(nr), keyboard);
	}

	@Override
	@External
	public void starPressed() {
		API.send(new StarPressed(), alarm);
	}

	@Override
	@External
	public void hashPressed() {
		API.send(new HashPressed(), alarm);
	}

}
