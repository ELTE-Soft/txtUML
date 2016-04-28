package clock.j.model.connectors;

import clock.j.model.associations.HourHand;
import clock.j.model.associations.MinuteHand;
import clock.j.model.classes.Hand;
import hu.elte.txtuml.api.model.Connector;

public class Hours extends Connector {
	public class minuteHand extends One<MinuteHand.minuteHand, Hand.OutTickPort> {}
	public class hourHand extends One<HourHand.hourHand, Hand.InTickPort> {}
}
