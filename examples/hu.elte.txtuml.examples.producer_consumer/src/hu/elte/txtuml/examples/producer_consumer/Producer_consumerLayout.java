package hu.elte.txtuml.examples.producer_consumer;

import hu.elte.txtuml.examples.producer_consumer.Producer_consumerModel.*;
import hu.elte.txtuml.layout.lang.Diagram;
import hu.elte.txtuml.layout.lang.statements.*;

class Producer_consumerDiagram extends Diagram
{
	@Row({Producer.class, Storage.class, Consumer.class})
	@South(val = Item.class, from = Storage.class)
    class Producer_consumerLayout extends Layout {}
}