package microwave.diagrams;

import hu.elte.txtuml.api.layout.Below;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import microwave.model.Clock;
import microwave.model.SimpleClock;

public class SimpleClockSMDiagram extends StateMachineDiagram<Clock> {
	@Row({ SimpleClock.Init.class, SimpleClock.Stopped.class, SimpleClock.Running.class })
	@Below(val = SimpleClock.Interrupted.class, from = SimpleClock.Stopped.class)
	class L extends Layout {
	}
}
