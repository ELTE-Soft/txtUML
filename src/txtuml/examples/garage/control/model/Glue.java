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

	// Linkage to the UI
	public IControlled controlled;
	public void setControlled(IControlled ctd) {
		controlled = ctd;
	}
	
	// Singleton pattern
	static Glue instance = null;
	
	private Glue() {
		Action.link(GarageModel.MotorMovesDoor.movedDoor.class, door, GarageModel.MotorMovesDoor.movingMotor.class, motor);
		Action.start(door);
		Action.start(motor);
	}

	public static synchronized Glue getInstance() {
		if(instance == null) {
			instance = new Glue();
		}
		return instance;
	}
	
	// IControl implementation
	@Override
	public void remoteControlButtonPressed() {
		Action.send(door,gmodel.new RemoteControlButtonPressed());
	}

	@Override
	public void motionSensorActivated() {
		Action.send(door, gmodel.new MotionSensorActivated());
	}

	@Override
	public void alarmSensorActivated() {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void starPressed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hashPressed() {
		// TODO Auto-generated method stub
		
	}
}
	