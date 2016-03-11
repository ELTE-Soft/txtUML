package clock.j.model.associations;

import clock.j.model.classes.Clock;
import clock.j.model.classes.Hand;
import hu.elte.txtuml.api.model.Composition;

public class HourHand extends Composition {
	public class clock extends HiddenContainer<Clock> {}
	public class hourHand extends One<Hand> {}
}
