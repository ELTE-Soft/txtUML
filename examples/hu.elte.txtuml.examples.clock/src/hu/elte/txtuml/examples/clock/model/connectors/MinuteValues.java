package hu.elte.txtuml.examples.clock.model.connectors;

import hu.elte.txtuml.api.model.Connector;
import hu.elte.txtuml.examples.clock.model.associations.DisplayInClock;
import hu.elte.txtuml.examples.clock.model.associations.MinuteHand;
import hu.elte.txtuml.examples.clock.model.classes.Display;
import hu.elte.txtuml.examples.clock.model.classes.Hand;

public class MinuteValues extends Connector {
	public class minuteHand extends One<MinuteHand.minuteHand, Hand.ValuePort> {}
	public class face extends One<DisplayInClock.face, Display.MinutePort> {}
}
