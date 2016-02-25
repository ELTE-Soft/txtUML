package hu.elte.txtuml.examples.producer_consumer;

import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.South;
import hu.elte.txtuml.examples.producer_consumer.xmodel.Consumer;
import hu.elte.txtuml.examples.producer_consumer.xmodel.Item;
import hu.elte.txtuml.examples.producer_consumer.xmodel.Producer;
import hu.elte.txtuml.examples.producer_consumer.xmodel.Storage;

class XProducerConsumerClassDiagram extends ClassDiagram {

	@Row({ Producer.class, Storage.class, Consumer.class })
	@South(val = Item.class, from = Storage.class)
	class ProducerConsumerLayout extends Layout {
	}
}
