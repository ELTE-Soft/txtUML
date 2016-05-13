package producer_consumer.x;

import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.South;
import producer_consumer.x.model.Consumer;
import producer_consumer.x.model.Item;
import producer_consumer.x.model.Producer;
import producer_consumer.x.model.Storage;

public class XProducerConsumerClassDiagram extends ClassDiagram {

	@Row({ Producer.class, Storage.class, Consumer.class })
	@South(val = Item.class, from = Storage.class)
	class ProducerConsumerLayout extends Layout {
	}
}
