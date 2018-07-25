package producer_consumer.j.diagrams;

import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import producer_consumer.j.model.Consumer;
import producer_consumer.j.model.Consumer.Active;
import producer_consumer.j.model.Consumer.Init;
import producer_consumer.j.model.Consumer.Passive;

public class ConsumerSMDiagram extends StateMachineDiagram<Consumer> {
	@Row({Init.class, Active.class, Passive.class})
	class L extends Layout{}
}
