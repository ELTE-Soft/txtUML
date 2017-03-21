package hu.elte.txtuml.examples.garage.control.model.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.stdlib.timers.Timer;
import hu.elte.txtuml.examples.garage.control.model.Door;

public class DoorUsesTimer extends Association {
	public class timer extends MaybeOne<Timer> {}
	public class door extends HiddenMaybeOne<Door> {}
}
