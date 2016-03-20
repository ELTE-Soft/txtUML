package clock.j;

import clock.j.model.classes.Clock;
import clock.j.model.classes.Hand;
import clock.j.model.classes.Pendulum;
import hu.elte.txtuml.api.layout.Above;
import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.Right;

class ClockDiagram extends ClassDiagram {
	@Above(val=Clock.class, from=Hand.class)
	@Right(val=Pendulum.class, from=Hand.class)
	class ClockLayout extends Layout {}
}
