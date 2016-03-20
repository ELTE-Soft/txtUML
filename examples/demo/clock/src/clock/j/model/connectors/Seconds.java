package clock.j.model.connectors;

import clock.j.model.associations.PendulumInClock;
import clock.j.model.associations.SecondHand;
import clock.j.model.classes.Hand;
import clock.j.model.classes.Pendulum;
import hu.elte.txtuml.api.model.Connector;

public class Seconds extends Connector {
	public class pendulum extends One<PendulumInClock.pendulum, Pendulum.OutTickPort> {}
	public class secondHand extends One<SecondHand.secondHand, Hand.InTickPort> {}
}
