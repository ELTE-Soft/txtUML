package clock.j.model.associations;

import clock.j.model.classes.Clock;
import clock.j.model.classes.Hand;
import hu.elte.txtuml.api.model.Composition;
import hu.elte.txtuml.api.model.One;

public class HourHand extends Composition {
	public class clock extends HiddenContainerEnd<Clock> {}
	public class hourHand extends End<One<Hand>> {}
}
