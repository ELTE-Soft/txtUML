package clock.x.diagrams;

import clock.x.model.Clock;
import clock.x.model.Display;
import clock.x.model.Hand;
import clock.x.model.Pendulum;
import hu.elte.txtuml.api.layout.Above;
import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.Left;
import hu.elte.txtuml.api.layout.Right;

public class XClockClassDiagram extends ClassDiagram {
	@Above(val=Clock.class, from=Hand.class)
	@Right(val=Pendulum.class, from=Hand.class)
	@Left(val=Display.class, from=Hand.class)
	class ClockLayout extends Layout {}
}
