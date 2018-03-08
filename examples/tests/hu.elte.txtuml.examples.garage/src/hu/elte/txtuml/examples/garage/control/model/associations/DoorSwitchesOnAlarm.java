package hu.elte.txtuml.examples.garage.control.model.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.One;
import hu.elte.txtuml.examples.garage.control.model.Alarm;
import hu.elte.txtuml.examples.garage.control.model.Door;

public class DoorSwitchesOnAlarm extends Association {
	public class SwitchingDoor extends End<One<Door>> {
	}

	public class SwitchedAlarm extends End<One<Alarm>> {
	}
}