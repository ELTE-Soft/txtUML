package hu.elte.txtuml.examples.garage.control.model.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.stdlib.timers.Timer;
import hu.elte.txtuml.examples.garage.control.model.Keyboard;

public class KeyboardUsesTimer extends Association {
		public class timer extends MaybeOne<Timer> {}
		public class keyboard extends HiddenMaybeOne<Keyboard> {}
}
