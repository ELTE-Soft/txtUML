package nuclearpower.diagrams;

import hu.elte.txtuml.api.layout.Above;
import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.Contains;
import hu.elte.txtuml.api.layout.North;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import nuclearpower.model.SolarPanel;
import nuclearpower.model.SolarPanel.InOperation;
import nuclearpower.model.SolarPanel.Init;
import nuclearpower.model.SolarPanel.NotInOperation;

//This diagram needs the support for hierarchy
public class SolarPanelSMDiagram extends StateMachineDiagram<SolarPanel>{
	
	@Contains({InOperation.ProducesPower.class, InOperation.ChargesBattery.class})
	class G extends NodeGroup{}
	
	@Row({Init.class, InOperation.class, NotInOperation.class})
	@Column({NotInOperation.Init.class, NotInOperation.Passive.class, NotInOperation.UsesBattery.class})
	@Above(from=InOperation.Init.class, val=InOperation.Passive.class)
	@North(from=G.class, val=InOperation.Passive.class)
	class L extends Layout{}
}
