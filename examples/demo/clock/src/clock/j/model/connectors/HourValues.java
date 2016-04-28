package clock.j.model.connectors;

import clock.j.model.associations.DisplayInClock;
import clock.j.model.associations.HourHand;
import clock.j.model.classes.Display;
import clock.j.model.classes.Hand;
import hu.elte.txtuml.api.model.Connector;

public class HourValues extends Connector {
	public class hourHand extends One<HourHand.hourHand, Hand.ValuePort> {}
	public class face extends One<DisplayInClock.face, Display.HourPort> {}
}
