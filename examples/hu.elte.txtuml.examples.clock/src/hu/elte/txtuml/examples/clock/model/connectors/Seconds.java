package hu.elte.txtuml.examples.clock.model.connectors;

import hu.elte.txtuml.api.model.Connector;
import hu.elte.txtuml.examples.clock.model.associations.PendulumInClock;
import hu.elte.txtuml.examples.clock.model.associations.SecondHand;
import hu.elte.txtuml.examples.clock.model.classes.Hand;
import hu.elte.txtuml.examples.clock.model.classes.Pendulum;

public class Seconds extends Connector {
	public class pendulum extends One<PendulumInClock.pendulum, Pendulum.OutTickPort> {}
	public class secondHand extends One<SecondHand.secondHand, Hand.InTickPort> {}
}
