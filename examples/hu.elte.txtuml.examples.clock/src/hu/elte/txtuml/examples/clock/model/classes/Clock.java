package hu.elte.txtuml.examples.clock.model.classes;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.examples.clock.model.associations.DisplayInClock;
import hu.elte.txtuml.examples.clock.model.associations.HourHand;
import hu.elte.txtuml.examples.clock.model.associations.MinuteHand;
import hu.elte.txtuml.examples.clock.model.associations.PendulumInClock;
import hu.elte.txtuml.examples.clock.model.associations.SecondHand;
import hu.elte.txtuml.examples.clock.model.classes.Display.HourPort;
import hu.elte.txtuml.examples.clock.model.classes.Display.MinutePort;
import hu.elte.txtuml.examples.clock.model.classes.Display.SecondPort;
import hu.elte.txtuml.examples.clock.model.classes.Hand.InTickPort;
import hu.elte.txtuml.examples.clock.model.classes.Hand.OutTickPort;
import hu.elte.txtuml.examples.clock.model.classes.Hand.ValuePort;
import hu.elte.txtuml.examples.clock.model.connectors.HourValues;
import hu.elte.txtuml.examples.clock.model.connectors.Hours;
import hu.elte.txtuml.examples.clock.model.connectors.MinuteValues;
import hu.elte.txtuml.examples.clock.model.connectors.Minutes;
import hu.elte.txtuml.examples.clock.model.connectors.SecondValues;
import hu.elte.txtuml.examples.clock.model.connectors.Seconds;

public class Clock extends ModelClass {
	public Clock(int hour, int minute, int second) {
		Pendulum pendulum = Action.create(Pendulum.class);
		Hand secondHand = new Hand(60,second);
		Hand minuteHand = new Hand(60,minute);
		Hand hourHand = new Hand(24,hour);
		Display display = new Display(hour, minute, second);
		Action.link(PendulumInClock.clock.class, this, PendulumInClock.pendulum.class, pendulum);
		Action.link(SecondHand.clock.class, this, SecondHand.secondHand.class, secondHand);
		Action.link(MinuteHand.clock.class, this, MinuteHand.minuteHand.class, minuteHand);
		Action.link(HourHand.clock.class, this, HourHand.hourHand.class, hourHand);
		Action.link(DisplayInClock.clock.class, this, DisplayInClock.face.class, display);
		Action.connect(Seconds.pendulum.class, pendulum.port(hu.elte.txtuml.examples.clock.model.classes.Pendulum.OutTickPort.class),
					Seconds.secondHand.class, secondHand.port(InTickPort.class));
		Action.connect(Minutes.secondHand.class, secondHand.port(OutTickPort.class),
				Minutes.minuteHand.class, minuteHand.port(InTickPort.class));
		Action.connect(Hours.minuteHand.class, minuteHand.port(OutTickPort.class),
				Hours.hourHand.class, hourHand.port(InTickPort.class));
		Action.connect(HourValues.hourHand.class, hourHand.port(ValuePort.class),
				HourValues.face.class, display.port(HourPort.class));
		Action.connect(MinuteValues.minuteHand.class, minuteHand.port(ValuePort.class),
				MinuteValues.face.class, display.port(MinutePort.class));
		Action.connect(SecondValues.secondHand.class, secondHand.port(ValuePort.class),
				SecondValues.face.class, display.port(SecondPort.class));
		Action.start(pendulum);
		Action.start(secondHand);
		Action.start(minuteHand);
		Action.start(hourHand);
		Action.start(display);
	}
}
