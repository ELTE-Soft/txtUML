package hu.elte.txtuml.examples.clock.model.connectors;

import hu.elte.txtuml.api.model.Connector;
import hu.elte.txtuml.examples.clock.model.associations.DisplayInClock;
import hu.elte.txtuml.examples.clock.model.associations.HourHand;
import hu.elte.txtuml.examples.clock.model.classes.Display;
import hu.elte.txtuml.examples.clock.model.classes.Hand;

public class HourValues extends Connector {
	public class hourHand extends One<HourHand.hourHand, Hand.ValuePort> {}
	public class face extends One<DisplayInClock.face, Display.HourPort> {}
}
