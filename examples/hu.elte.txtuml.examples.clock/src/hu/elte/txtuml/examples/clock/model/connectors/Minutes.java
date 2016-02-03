package hu.elte.txtuml.examples.clock.model.connectors;

import hu.elte.txtuml.examples.clock.model.associations.MinuteHand;
import hu.elte.txtuml.examples.clock.model.associations.SecondHand;
import hu.elte.txtuml.examples.clock.model.classes.Hand;
import hu.elte.txtuml.api.model.Connector;

public class Minutes extends Connector {
	public class secondHand extends One<SecondHand.secondHand, Hand.OutTickPort> {}
	public class minuteHand extends One<MinuteHand.minuteHand, Hand.InTickPort> {}
}
