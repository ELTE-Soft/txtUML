package clock.j.model.connectors;

import clock.j.model.associations.MinuteHand;
import clock.j.model.associations.SecondHand;
import clock.j.model.classes.Hand;
import hu.elte.txtuml.api.model.Connector;

public class Minutes extends Connector {
	public class secondHand extends One<SecondHand.secondHand, Hand.OutTickPort> {}
	public class minuteHand extends One<MinuteHand.minuteHand, Hand.InTickPort> {}
}
