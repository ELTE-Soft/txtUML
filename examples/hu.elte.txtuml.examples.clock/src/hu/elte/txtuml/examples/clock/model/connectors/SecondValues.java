package hu.elte.txtuml.examples.clock.model.connectors;

import hu.elte.txtuml.api.model.Connector;
import hu.elte.txtuml.examples.clock.model.associations.DisplayInClock;
import hu.elte.txtuml.examples.clock.model.associations.SecondHand;
import hu.elte.txtuml.examples.clock.model.classes.Display;
import hu.elte.txtuml.examples.clock.model.classes.Hand;

public class SecondValues extends Connector {
	public class secondHand extends One<SecondHand.secondHand, Hand.ValuePort> {}
	public class face extends One<DisplayInClock.face, Display.SecondPort> {}
}
