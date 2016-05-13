package clock.j.model.classes;

import clock.j.model.associations.DisplayInClock;
import clock.j.model.associations.HourHand;
import clock.j.model.associations.MinuteHand;
import clock.j.model.associations.PendulumInClock;
import clock.j.model.associations.SecondHand;
import clock.j.model.classes.Display.HourPort;
import clock.j.model.classes.Display.MinutePort;
import clock.j.model.classes.Display.SecondPort;
import clock.j.model.classes.Hand.InTickPort;
import clock.j.model.classes.Hand.OutTickPort;
import clock.j.model.classes.Hand.ValuePort;
import clock.j.model.connectors.HourValues;
import clock.j.model.connectors.Hours;
import clock.j.model.connectors.MinuteValues;
import clock.j.model.connectors.Minutes;
import clock.j.model.connectors.SecondValues;
import clock.j.model.connectors.Seconds;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.Interface;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Port;

public class Clock extends ModelClass {
	
	public class MyPort extends Port<Interface.Empty, Interface.Empty>{}
	
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
		Action.connect(Seconds.pendulum.class, pendulum.port(clock.j.model.classes.Pendulum.OutTickPort.class),
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
