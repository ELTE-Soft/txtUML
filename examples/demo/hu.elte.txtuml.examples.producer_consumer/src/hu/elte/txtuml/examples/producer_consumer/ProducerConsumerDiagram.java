package hu.elte.txtuml.examples.producer_consumer;

import hu.elte.txtuml.api.layout.Diagram;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.South;
import hu.elte.txtuml.examples.producer_consumer.model.Consumer;
import hu.elte.txtuml.examples.producer_consumer.model.Item;
import hu.elte.txtuml.examples.producer_consumer.model.Producer;
import hu.elte.txtuml.examples.producer_consumer.model.Storage;

class ProducerConsumerDiagram extends Diagram {

	@Row({ Producer.class, Storage.class, Consumer.class })
	@South(val = Item.class, from = Storage.class)
	class ProducerConsumerLayout extends Layout {
	}
}
