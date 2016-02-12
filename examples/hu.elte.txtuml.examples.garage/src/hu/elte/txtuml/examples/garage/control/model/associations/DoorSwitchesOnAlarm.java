package hu.elte.txtuml.examples.garage.control.model.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.examples.garage.control.model.Alarm;
import hu.elte.txtuml.examples.garage.control.model.Door;

public class DoorSwitchesOnAlarm extends Association {
	public class SwitchingDoor extends One<Door> {
	}

	public class SwitchedAlarm extends One<Alarm> {
	}
}