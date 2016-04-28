package clock.j;

import hu.elte.txtuml.api.layout.CompositeDiagram;
import hu.elte.txtuml.api.layout.East;
import hu.elte.txtuml.api.layout.North;
import hu.elte.txtuml.api.layout.Show;
import hu.elte.txtuml.api.layout.Spacing;
import clock.j.model.associations.HourHand;
import clock.j.model.associations.MinuteHand;
import clock.j.model.associations.SecondHand;
import clock.j.model.classes.Clock;

public class ClockCompositeDiagram extends CompositeDiagram<Clock> {
	@East(from=HourHand.hourHand.class, val=MinuteHand.minuteHand.class)
	@North(from=MinuteHand.minuteHand.class, val=SecondHand.secondHand.class)
	@Spacing(0.4)
	class ClockLayout extends Layout {}
}
