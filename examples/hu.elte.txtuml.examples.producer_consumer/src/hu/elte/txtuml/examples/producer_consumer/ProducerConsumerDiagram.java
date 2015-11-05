package hu.elte.txtuml.examples.producer_consumer;

import hu.elte.txtuml.api.layout.*;
import hu.elte.txtuml.examples.producer_consumer.XProducerConsumer.*;

class ProducerConsumerDiagram extends Diagram
{
	@Row({Producer.class, Storage.class, Consumer.class})
	@South(val = Item.class, from = Storage.class)
    class ProducerConsumerLayout extends Layout {}
}
