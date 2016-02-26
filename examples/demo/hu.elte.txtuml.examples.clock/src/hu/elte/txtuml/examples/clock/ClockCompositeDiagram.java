package hu.elte.txtuml.examples.clock;

import hu.elte.txtuml.api.layout.CompositeDiagram;
import hu.elte.txtuml.api.layout.Show;
import hu.elte.txtuml.examples.clock.model.associations.HourHand;
import hu.elte.txtuml.examples.clock.model.classes.Clock;

public class ClockCompositeDiagram extends CompositeDiagram<Clock> {
	@Show(HourHand.hourHand.class)
	class ClockLayout extends Layout {}
}
