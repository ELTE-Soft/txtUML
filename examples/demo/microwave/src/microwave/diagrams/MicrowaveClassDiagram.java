package microwave.diagrams;

import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.Show;
import hu.elte.txtuml.api.layout.Spacing;
import hu.elte.txtuml.api.layout.TopMost;
import microwave.model.Clock;
import microwave.model.Door;
import microwave.model.MicrowaveOven;
import microwave.model.SimpleClock;
import microwave.model.SmartClock;

public class MicrowaveClassDiagram extends ClassDiagram {
	@Spacing(0.5)
	@TopMost(MicrowaveOven.class)
	@Row({Door.class, SimpleClock.class, SmartClock.class})
	@Show(Clock.class)
	class MicrowaveLayout extends Layout {}
}