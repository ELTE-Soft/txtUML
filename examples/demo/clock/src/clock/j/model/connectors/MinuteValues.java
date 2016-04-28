package clock.j.model.connectors;

import clock.j.model.associations.DisplayInClock;
import clock.j.model.associations.MinuteHand;
import clock.j.model.classes.Display;
import clock.j.model.classes.Hand;
import hu.elte.txtuml.api.model.Connector;

public class MinuteValues extends Connector {
	public class minuteHand extends One<MinuteHand.minuteHand, Hand.ValuePort> {}
	public class face extends One<DisplayInClock.face, Display.MinutePort> {}
}
