package hu.elte.txtuml.examples.clock.model.connectors;

import hu.elte.txtuml.examples.clock.model.associations.HourHand;
import hu.elte.txtuml.examples.clock.model.associations.MinuteHand;
import hu.elte.txtuml.examples.clock.model.classes.Hand;
import hu.elte.txtuml.api.model.Connector;

public class Hours extends Connector {
	public class minuteHand extends One<MinuteHand.minuteHand, Hand.OutTickPort> {}
	public class hourHand extends One<HourHand.hourHand, Hand.InTickPort> {}
}
