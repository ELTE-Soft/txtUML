package hu.elte.txtuml.examples.producer_consumer;

import hu.elte.txtuml.api.layout.Diagram;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.South;
import hu.elte.txtuml.examples.producer_consumer.Producer_consumerModel.Consumer;
import hu.elte.txtuml.examples.producer_consumer.Producer_consumerModel.Item;
import hu.elte.txtuml.examples.producer_consumer.Producer_consumerModel.Producer;
import hu.elte.txtuml.examples.producer_consumer.Producer_consumerModel.Storage;

class Producer_consumerDiagram extends Diagram
{
	@Row({Producer.class, Storage.class, Consumer.class})
	@South(val = Item.class, from = Storage.class)
    class Producer_consumerLayout extends Layout {}
}