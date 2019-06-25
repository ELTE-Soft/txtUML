package producer_consumer.j.tests;

import static hu.elte.txtuml.api.model.seqdiag.Sequence.assertState;
import static hu.elte.txtuml.api.model.seqdiag.Sequence.assertSend;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.seqdiag.Lifeline;
import hu.elte.txtuml.api.model.seqdiag.Position;
import hu.elte.txtuml.api.model.seqdiag.Sequence;
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
	Lifeline<Storage> storage;

	@Position(1)
	Lifeline<Producer> p1;

	@Position(1)
	Lifeline<Producer> p2;

	@Position(3)
	Lifeline<Consumer> c1;

	@Position(3)
	Lifeline<Consumer> c2;

	@Position(3)
	Lifeline<Consumer> c3;

	@Override
	public void initialize() {
		Storage s = Action.create(Storage.class, Integer.valueOf(2));
		Producer prod1 = Action.create(Producer.class, Integer.valueOf(3));
		Producer prod2 = Action.create(Producer.class, Integer.valueOf(3));
		Consumer cons1 = Action.create(Consumer.class, Integer.valueOf(2));
		Consumer cons2 = Action.create(Consumer.class, Integer.valueOf(2));
		Consumer cons3 = Action.create(Consumer.class, Integer.valueOf(2));

		Action.link(Production.producer.class, prod1, Production.storage.class, s);
		Action.link(Production.producer.class, prod2, Production.storage.class, s);
		Action.link(Consumption.consumer.class, cons1, Consumption.storage.class, s);
		Action.link(Consumption.consumer.class, cons2, Consumption.storage.class, s);
		Action.link(Consumption.consumer.class, cons3, Consumption.storage.class, s);

		storage = Sequence.createLifeline(s);
		p1 = Sequence.createLifeline(prod1);
		p2 = Sequence.createLifeline(prod2);
		c1 = Sequence.createLifeline(cons1);
		c2 = Sequence.createLifeline(cons2);
		c3 = Sequence.createLifeline(cons3);

		Action.start(s);
		Action.start(prod1);
		Action.start(prod2);
		Action.start(cons1);
		Action.start(cons2);
		Action.start(cons3);
	}

	protected void produce(Lifeline<Producer> p) {
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

	protected void consume(Lifeline<Consumer> c) {
		assertSend(c, new RequestNotification(), storage);
		assertSend(c, new DoWork(), c);
		assertState(c, Consumer.Active.class);
		assertState(storage, Storage.Working.class);

		assertSend(c, new RequestNotification(), storage);
		assertSend(c, new DoWork(), c);
		assertState(c, Consumer.Passive.class);
	}

}
