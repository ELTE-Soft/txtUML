package hu.elte.txtuml.examples.garage.control.model.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.examples.garage.control.model.Alarm;
import hu.elte.txtuml.examples.garage.control.model.Keyboard;

public class KeyboardProvidesCode extends Association {
	public class Provider extends One<Keyboard> {
	}

	public class Receiver extends One<Alarm> {
	}
}