package txtuml.examples.garage.control.model;

import txtuml.api.*;
import txtuml.examples.garage.interfaces.IControl;
import txtuml.examples.garage.interfaces.IControlled;

// This class is the glue code between the UI and the control model
public class Glue implements IControl {
	// Model instantiation
	GarageModel gmodel = new GarageModel();
	GarageModel.Door door = gmodel.new Door();
	GarageModel.Motor motor = gmodel.new Motor();
	GarageModel.Alarm alarm = gmodel.new Alarm();
	GarageModel.Keyboard keyboard = gmodel.new Keyboard();

	// Linkage to the UI
	public IControlled controlled;
	public void setControlled(IControlled ctd) {
		controlled = ctd;
	}
	
	// Singleton pattern
	static Glue instance = null;
	
	private Glue() {
		// Initialize links and start object instances
		Action.link(GarageModel.MotorMovesDoor.movedDoor.class, door, GarageModel.MotorMovesDoor.movingMotor.class, motor);
		Action.link(GarageModel.DoorSwitchesOnAlarm.SwitchingDoor.class, door, GarageModel.DoorSwitchesOnAlarm.SwitchedAlarm.class, alarm);
		Action.link(GarageModel.KeyboardProvidesCode.Provider.class, keyboard, GarageModel.KeyboardProvidesCode.Receiver.class, alarm);
		Action.start(door);
		Action.start(motor);
		Action.start(alarm);
		Action.start(keyboard);
	}

	public static synchronized Glue getInstance() {
		if(instance == null) {
			instance = new Glue();
		}
		return instance;
	}
	
	// IControl implementation
	@Override public void remoteControlButtonPressed() {
		Action.send(door,gmodel.new RemoteControlButtonPressed());
	}

	@Override public void motionSensorActivated() {
		Action.send(door, gmodel.new MotionSensorActivated());
	}

	@Override public void alarmSensorActivated() {
		Action.send(alarm, gmodel.new AlarmSensorActivated());
	}

	@Override
	public void doorReachedTop() {
		Action.send(motor, gmodel.new DoorReachedTop());
	}

	@Override
	public void doorReachedBottom() {
		Action.send(motor, gmodel.new DoorReachedBottom());
	}

	@Override
	public void keyPress(int nr) {
		Action.send(keyboard, gmodel.new KeyPress(new ModelInt(nr)));
	}

	@Override
	public void starPressed() {
		Action.send(alarm, gmodel.new StarPressed());
	}

	@Override
	public void hashPressed() {
		Action.send(alarm, gmodel.new HashPressed());
	}
}
	