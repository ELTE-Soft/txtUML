package hu.elte.txtuml.examples.clock.model.classes;

import hu.elte.txtuml.examples.clock.model.associations.HourHand;
import hu.elte.txtuml.examples.clock.model.associations.MinuteHand;
import hu.elte.txtuml.examples.clock.model.associations.PendulumInClock;
import hu.elte.txtuml.examples.clock.model.associations.SecondHand;
import hu.elte.txtuml.examples.clock.model.classes.Hand.InTickPort;
import hu.elte.txtuml.examples.clock.model.classes.Hand.OutTickPort;
import hu.elte.txtuml.examples.clock.model.connectors.Hours;
import hu.elte.txtuml.examples.clock.model.connectors.Minutes;
import hu.elte.txtuml.examples.clock.model.connectors.Seconds;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelClass;

public class Clock extends ModelClass {
	public Clock() {
		Pendulum pendulum = Action.create(Pendulum.class);
		Hand secondHand = new Hand(60,2);
		Hand minuteHand = new Hand(60,1);
		Hand hourHand = new Hand(24,0);
		Action.link(PendulumInClock.clock.class, this, PendulumInClock.pendulum.class, pendulum);
		Action.link(SecondHand.clock.class, this, SecondHand.secondHand.class, secondHand);
		Action.link(MinuteHand.clock.class, this, MinuteHand.minuteHand.class, minuteHand);
		Action.link(HourHand.clock.class, this, HourHand.hourHand.class, hourHand);
		Action.connect(Seconds.pendulum.class, pendulum.port(hu.elte.txtuml.examples.clock.model.classes.Pendulum.OutTickPort.class),
					Seconds.secondHand.class, secondHand.port(InTickPort.class));
		Action.connect(Minutes.secondHand.class, secondHand.port(OutTickPort.class),
				Minutes.minuteHand.class, minuteHand.port(InTickPort.class));
		Action.connect(Hours.minuteHand.class, minuteHand.port(OutTickPort.class),
				Hours.hourHand.class, hourHand.port(InTickPort.class));
		Action.start(pendulum);
		Action.start(secondHand);
		Action.start(minuteHand);
		Action.start(hourHand);
	}
}
