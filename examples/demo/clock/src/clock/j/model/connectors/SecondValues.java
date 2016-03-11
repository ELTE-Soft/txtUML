package clock.j.model.connectors;

import clock.j.model.associations.DisplayInClock;
import clock.j.model.associations.SecondHand;
import clock.j.model.classes.Display;
import clock.j.model.classes.Hand;
import hu.elte.txtuml.api.model.Connector;

public class SecondValues extends Connector {
	public class secondHand extends One<SecondHand.secondHand, Hand.ValuePort> {}
	public class face extends One<DisplayInClock.face, Display.SecondPort> {}
}
