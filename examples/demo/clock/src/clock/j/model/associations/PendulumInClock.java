package clock.j.model.associations;

import clock.j.model.classes.Clock;
import clock.j.model.classes.Pendulum;
import hu.elte.txtuml.api.model.Composition;
import hu.elte.txtuml.api.model.One;

public class PendulumInClock extends Composition {
	public class clock extends HiddenContainerEnd<Clock> {}
	public class pendulum extends End<One<Pendulum>> {}
}
