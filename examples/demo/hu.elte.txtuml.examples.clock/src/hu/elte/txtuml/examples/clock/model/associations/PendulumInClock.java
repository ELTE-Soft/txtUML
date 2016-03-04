package hu.elte.txtuml.examples.clock.model.associations;

import hu.elte.txtuml.examples.clock.model.classes.Clock;
import hu.elte.txtuml.examples.clock.model.classes.Pendulum;
import hu.elte.txtuml.api.model.Composition;

public class PendulumInClock extends Composition {
	public class clock extends HiddenContainer<Clock> {}
	public class pendulum extends One<Pendulum> {}
}
