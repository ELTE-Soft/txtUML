package hu.elte.txtuml.examples.producer_consumer;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelExecutor;
import hu.elte.txtuml.examples.producer_consumer.xmodel.Consumer;
import hu.elte.txtuml.examples.producer_consumer.xmodel.Consumption;
import hu.elte.txtuml.examples.producer_consumer.xmodel.Producer;
import hu.elte.txtuml.examples.producer_consumer.xmodel.Production;
import hu.elte.txtuml.examples.producer_consumer.xmodel.Storage;

public class XProducerConsumerTester {

	public static void main(final String... args) {
		ModelExecutor.Settings.setExecutorLog(true);
		Storage storage = Action.create(Storage.class, Integer.valueOf(2));
		Producer p1 = Action.create(Producer.class, Integer.valueOf(3));
		Producer p2 = Action.create(Producer.class, Integer.valueOf(3));
		Consumer c1 = Action.create(Consumer.class, Integer.valueOf(2));
		Consumer c2 = Action.create(Consumer.class, Integer.valueOf(2));
		Consumer c3 = Action.create(Consumer.class, Integer.valueOf(2));
		Action.link(Production.producer.class, p1, Production.storage.class, storage);
		Action.link(Production.producer.class, p2, Production.storage.class, storage);
		Action.link(Consumption.consumer.class, c1, Consumption.storage.class, storage);
		Action.link(Consumption.consumer.class, c2, Consumption.storage.class, storage);
		Action.link(Consumption.consumer.class, c3, Consumption.storage.class, storage);
		Action.start(storage);
		Action.start(p1);
		Action.start(p2);
		Action.start(c1);
		Action.start(c2);
		Action.start(c3);
		ModelExecutor.shutdown();
	}
}
