package producer_consumer.x;

import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import producer_consumer.x.model.Producer;
import producer_consumer.x.model.Producer.Active;
import producer_consumer.x.model.Producer.Init;
import producer_consumer.x.model.Producer.Passive;

public class XProducerSMDiagram extends StateMachineDiagram<Producer>{
	@Row({Init.class, Active.class, Passive.class})
	class L extends Layout{}
}
