package microwave.diagrams;

import hu.elte.txtuml.api.layout.Below;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import microwave.model.Clock;
import microwave.model.SmartClock;

public class SmartClockSMDiagram extends StateMachineDiagram<Clock> {
	@Row({ SmartClock.Init.class, SmartClock.Stopped.class, SmartClock.Running.class })
	@Below(val = SmartClock.Interrupted.class, from = SmartClock.Stopped.class)
	@Below(val = SmartClock.IsOver.class, from = SmartClock.Running.class)
	class L extends Layout {
	}
}
