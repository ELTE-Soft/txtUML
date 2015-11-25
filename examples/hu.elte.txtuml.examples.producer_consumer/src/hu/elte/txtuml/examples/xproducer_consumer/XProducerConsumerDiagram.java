package hu.elte.txtuml.examples.xproducer_consumer;

import hu.elte.txtuml.api.layout.Diagram;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.South;

class XProducerConsumerDiagram extends Diagram {

	@Row({ Producer.class, Storage.class, Consumer.class })
	@South(val = Item.class, from = Storage.class)
	class ProducerConsumerLayout extends Layout {
	}
}
