package producer_consumer.x;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.Execution;
import hu.elte.txtuml.api.model.execution.LogLevel;
import producer_consumer.x.model.Consumer;
import producer_consumer.x.model.Consumption;
import producer_consumer.x.model.Producer;
import producer_consumer.x.model.Production;
import producer_consumer.x.model.Storage;

public class Tester implements Execution {

	@Override
	public void configure(Settings s) {
		s.logLevel = LogLevel.TRACE;
	}

	@Override
	public void initialization() {
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
	}

	public static void main(String[] args) {
		new Tester().run();
	}
}
