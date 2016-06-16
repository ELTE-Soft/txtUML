package producer_consumer.j;

import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import producer_consumer.j.model.Producer;
import producer_consumer.j.model.Producer.Active;
import producer_consumer.j.model.Producer.Init;
import producer_consumer.j.model.Producer.Passive;

public class ProducerSMDiagram extends StateMachineDiagram<Producer>{
	@Row({Init.class, Active.class, Passive.class})
	class L extends Layout{}
}
