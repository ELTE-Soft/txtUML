package nuclearpower;

import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.Show;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import nuclearpower.model.NuclearPowerPlant;
import nuclearpower.model.NuclearPowerPlant.Init;
import nuclearpower.model.NuclearPowerPlant.NotInOperation;
import nuclearpower.model.NuclearPowerPlant.ProducePower;

public class PowerPlantSMDiagram extends StateMachineDiagram<NuclearPowerPlant>{
	
	@Row({Init.class, NotInOperation.class, ProducePower.class})
	@Show({ProducePower.FewPowerProducing.class, ProducePower.MorePowerProducing.class})
	class L extends Layout{
		
	}
}
