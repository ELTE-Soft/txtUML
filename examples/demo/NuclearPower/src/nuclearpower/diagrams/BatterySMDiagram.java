package nuclearpower.diagrams;

import hu.elte.txtuml.api.layout.Below;
import hu.elte.txtuml.api.layout.LeftMost;
import hu.elte.txtuml.api.layout.Right;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import nuclearpower.model.Battery;
import nuclearpower.model.Battery.Charging;
import nuclearpower.model.Battery.Init;
import nuclearpower.model.Battery.Passive;
import nuclearpower.model.Battery.ProvidesEnergy;

public class BatterySMDiagram extends StateMachineDiagram<Battery>{
	
	@LeftMost(Init.class)
	@Right(from=Init.class, val=Passive.class)
	@Right(from = Passive.class, val = Charging.class)
	@Below(from = Charging.class, val = ProvidesEnergy.class)
	class L extends Layout{}
}
