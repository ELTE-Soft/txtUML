package clock.j.model.associations;

import clock.j.model.classes.Clock;
import clock.j.model.classes.Hand;
import hu.elte.txtuml.api.model.Composition;
import hu.elte.txtuml.api.model.One;

public class MinuteHand extends Composition {
	public class clock extends HiddenContainerEnd<Clock> {}
	public class minuteHand extends End<One<Hand>> {}
}
