package hu.elte.txtuml.examples.garage.control.model.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.examples.garage.control.model.Door;
import hu.elte.txtuml.examples.garage.control.model.Motor;

public class MotorMovesDoor extends Association {
	public class movedDoor extends One<Door> {
	}

	public class movingMotor extends One<Motor> {
	}
}