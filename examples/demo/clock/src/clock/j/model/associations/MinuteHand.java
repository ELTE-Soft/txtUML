package clock.j.model.associations;

import clock.j.model.classes.Clock;
import clock.j.model.classes.Hand;
import hu.elte.txtuml.api.model.Composition;

public class MinuteHand extends Composition {
	public class clock extends HiddenContainer<Clock> {}
	public class minuteHand extends One<Hand> {}
}
