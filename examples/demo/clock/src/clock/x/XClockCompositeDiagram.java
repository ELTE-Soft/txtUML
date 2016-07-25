package clock.x;

import hu.elte.txtuml.api.layout.CompositeDiagram;
import hu.elte.txtuml.api.layout.East;
import hu.elte.txtuml.api.layout.North;
import hu.elte.txtuml.api.layout.Spacing;
import clock.x.model.associations.HourHand;
import clock.x.model.associations.MinuteHand;
import clock.x.model.associations.SecondHand;
import clock.x.model.Clock;

public class XClockCompositeDiagram extends CompositeDiagram<Clock> {
	@East(from=HourHand.hourHand.class, val=MinuteHand.minuteHand.class)
	@North(from=MinuteHand.minuteHand.class, val=SecondHand.secondHand.class)
	@Spacing(0.4)
	class ClockLayout extends Layout {}
}
