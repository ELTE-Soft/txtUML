package producer_consumer.j;

import hu.elte.txtuml.api.layout.Diagram;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.South;
import producer_consumer.j.model.Consumer;
import producer_consumer.j.model.Item;
import producer_consumer.j.model.Producer;
import producer_consumer.j.model.Storage;

public class ClassDiagram extends Diagram {

	@Row({ Producer.class, Storage.class, Consumer.class })
	@South(val = Item.class, from = Storage.class)
	class ProducerConsumerLayout extends Layout {
	}
}
