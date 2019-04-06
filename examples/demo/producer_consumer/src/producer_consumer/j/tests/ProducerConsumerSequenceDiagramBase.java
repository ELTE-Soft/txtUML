package producer_consumer.j.tests;

import static hu.elte.txtuml.api.model.seqdiag.Sequence.assertState;
import static hu.elte.txtuml.api.model.seqdiag.Sequence.assertSend;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.seqdiag.Position;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import producer_consumer.j.model.Consumer;
import producer_consumer.j.model.Consumption;
import producer_consumer.j.model.DoWork;
import producer_consumer.j.model.OfferNotification;
import producer_consumer.j.model.Producer;
import producer_consumer.j.model.Production;
import producer_consumer.j.model.RequestNotification;
import producer_consumer.j.model.Storage;

public abstract class ProducerConsumerSequenceDiagramBase extends SequenceDiagram {

	@Position(2)
	Storage storage;

	@Position(1)
	Producer p1;

	@Position(1)
	Producer p2;

	@Position(3)
	Consumer c1;

	@Position(3)
	Consumer c2;

	@Position(3)
	Consumer c3;

	@Override
	public void initialize() {
		storage = Action.create(Storage.class, Integer.valueOf(2));
		p1 = Action.create(Producer.class, Integer.valueOf(3));
		p2 = Action.create(Producer.class, Integer.valueOf(3));
		c1 = Action.create(Consumer.class, Integer.valueOf(2));
		c2 = Action.create(Consumer.class, Integer.valueOf(2));
		c3 = Action.create(Consumer.class, Integer.valueOf(2));

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
	
	protected void produce(Producer p) {
		for (int i = 0; i < 2; ++i) {
			assertSend(p, new OfferNotification(), storage);
			assertSend(p, new DoWork(), p);
			assertState(p, Producer.Active.class);
			assertState(storage, Storage.Working.class);
		}
		assertSend(p, new OfferNotification(), storage);
		assertSend(p, new DoWork(), p);
		assertState(p, Producer.Passive.class);
	}

	protected void consume(Consumer c) {
		assertSend(c, new RequestNotification(), storage);
		assertSend(c, new DoWork(), c);
		assertState(c, Consumer.Active.class);
		assertState(storage, Storage.Working.class);

		assertSend(c, new RequestNotification(), storage);
		assertSend(c, new DoWork(), c);
		assertState(c, Consumer.Passive.class);
	}

}
