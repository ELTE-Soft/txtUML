package producer_consumer.x;

import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import producer_consumer.x.model.Consumer;
import producer_consumer.x.model.Consumer.Active;
import producer_consumer.x.model.Consumer.Init;
import producer_consumer.x.model.Consumer.Passive;

class XConsumerSM extends StateMachineDiagram<Consumer> {
	@Row({Init.class, Active.class, Passive.class})
	class L extends Layout{}
}
