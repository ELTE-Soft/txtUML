package hu.elte.txtuml.examples.clock.model.associations;

import hu.elte.txtuml.examples.clock.model.classes.Clock;
import hu.elte.txtuml.examples.clock.model.classes.Hand;
import hu.elte.txtuml.api.model.Composition;

public class MinuteHand extends Composition {
	public class clock extends HiddenContainer<Clock> {}
	public class minuteHand extends One<Hand> {}
}
