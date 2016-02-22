package hu.elte.txtuml.examples.clock.model.associations;

import hu.elte.txtuml.api.model.Composition;
import hu.elte.txtuml.examples.clock.model.classes.Clock;
import hu.elte.txtuml.examples.clock.model.classes.Display;

public class DisplayInClock extends Composition {
	public class clock extends HiddenContainer<Clock> {}
	public class face extends One<Display> {}
}
