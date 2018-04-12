package microwave.diagrams;

import hu.elte.txtuml.api.layout.Below;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import microwave.model.SimpleClock;

public class SimpleClockSMDiagram extends StateMachineDiagram<SimpleClock> {
	@Row({ SimpleClock.Init.class, SimpleClock.Stopped.class, SimpleClock.Running.class })
	@Below(val = SimpleClock.Interrupted.class, from = SimpleClock.Stopped.class)
	class L extends Layout {
	}
}
