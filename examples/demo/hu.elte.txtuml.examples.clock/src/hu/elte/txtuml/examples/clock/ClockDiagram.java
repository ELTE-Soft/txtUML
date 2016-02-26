package hu.elte.txtuml.examples.clock;

import hu.elte.txtuml.api.layout.Above;
import hu.elte.txtuml.api.layout.Diagram;
import hu.elte.txtuml.api.layout.Right;
import hu.elte.txtuml.examples.clock.model.classes.Clock;
import hu.elte.txtuml.examples.clock.model.classes.Hand;
import hu.elte.txtuml.examples.clock.model.classes.Pendulum;

public class ClockDiagram extends Diagram {
	@Above(val=Clock.class, from=Hand.class)
	@Right(val=Pendulum.class, from=Hand.class)
	class ClockLayout extends Layout {}
}
