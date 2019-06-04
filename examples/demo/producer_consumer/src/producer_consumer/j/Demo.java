package producer_consumer.j;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.Execution;
import hu.elte.txtuml.api.model.execution.LogLevel;
import producer_consumer.j.model.C1;
import producer_consumer.j.model.Consumer;
import producer_consumer.j.model.Consumption;
import producer_consumer.j.model.Producer;
import producer_consumer.j.model.Production;
import producer_consumer.j.model.Storage;

public class Demo implements Execution {

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
		
		C1<Consumer> coll = Action.collectIn(C1.class, c1, c2, c3);
		
		Action.link(Production.producer.class, p1, Production.storage.class, storage);
		Action.link(Production.producer.class, p2, Production.storage.class, storage);
		for (Consumer c : coll) {
			Action.link(Consumption.consumer.class, c, Consumption.storage.class, storage);
		}
		
		Action.start(storage);
		Action.start(p1);
		Action.start(p2);
		for (Consumer c : coll) {
			Action.start(c);
		}
	}

	public static void main(String[] args) {
		new Demo().run();
	}
}
