package airlock.diagrams;

import airlock.model.Airlock;
import airlock.model.Airlock.Init;
import airlock.model.Airlock.InnerDoorOpen;
import airlock.model.Airlock.OuterDoorOpen;
import hu.elte.txtuml.api.layout.North;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;

public class AirlockSMDiagram extends StateMachineDiagram<Airlock>{
	@Row({InnerDoorOpen.class, OuterDoorOpen.class})
	@North(from = { InnerDoorOpen.class }, val = { Init.class })
	class L extends Layout{}
}
