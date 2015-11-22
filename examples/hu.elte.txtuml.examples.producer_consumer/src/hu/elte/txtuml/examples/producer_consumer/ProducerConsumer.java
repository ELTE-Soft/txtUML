package hu.elte.txtuml.examples.producer_consumer;

import hu.elte.txtuml.api.model.*;

class ProducerConsumer extends Model {
	public static class DoWork extends Signal {
	}

	public static class Acknowledgement extends Signal {
	}

	public class Producer extends ModelClass {
		int toProduce;

		Producer(final int num) {
			this.toProduce = num;
		}

		ProducerConsumer.Item produce() {
			this.toProduce--;
			Action.send(this, new ProducerConsumer.DoWork());
			return new ProducerConsumer.Item();
		}

		public class Init extends StateMachine.Initial {
		}

		public class Active extends StateMachine.State {
		}

		public class Passive extends StateMachine.State {
			@Override
			public void entry() {
				ProducerConsumer.Storage storage = Producer.this
						.assoc(hu.elte.txtuml.examples.producer_consumer.ProducerConsumer.Production.storage.class)
						.selectAny();
				Action.unlink(
						ProducerConsumer.Production.producer.class,
						Producer.this,
						hu.elte.txtuml.examples.producer_consumer.ProducerConsumer.Production.storage.class,
						storage);
				Action.delete(Producer.this);
			}
		}

		@From(ProducerConsumer.Producer.Init.class)
		@To(ProducerConsumer.Producer.Active.class)
		public class Initialize extends StateMachine.Transition {
			@Override
			public void effect() {
				Action.send(Producer.this, new ProducerConsumer.DoWork());
			}
		}

		@From(ProducerConsumer.Producer.Active.class)
		@To(ProducerConsumer.Producer.Active.class)
		@Trigger(ProducerConsumer.DoWork.class)
		public class DoOffer extends StateMachine.Transition {
			@Override
			public boolean guard() {
				return (Producer.this.toProduce > 0);
			}

			@Override
			public void effect() {
				ProducerConsumer.Storage storage = Producer.this
						.assoc(hu.elte.txtuml.examples.producer_consumer.ProducerConsumer.Production.storage.class)
						.selectAny();
				Action.link(
						ProducerConsumer.Offer.producer.class,
						Producer.this,
						hu.elte.txtuml.examples.producer_consumer.ProducerConsumer.Offer.storage.class,
						storage);
				Action.send(storage, new ProducerConsumer.OfferNotification());
			}
		}

		@From(ProducerConsumer.Producer.Active.class)
		@To(ProducerConsumer.Producer.Passive.class)
		@Trigger(ProducerConsumer.DoWork.class)
		public class Stop extends StateMachine.Transition {
			@Override
			public boolean guard() {
				return (Producer.this.toProduce <= 0);
			}
		}
	}

	public class Consumer extends ModelClass {
		int toConsume;

		Consumer(final int toConsume) {
			this.toConsume = toConsume;
		}

		void consume(final ProducerConsumer.Item item) {
			this.toConsume--;
			Action.send(this, new ProducerConsumer.DoWork());
		}

		public class Init extends StateMachine.Initial {
		}

		public class Active extends StateMachine.State {
		}

		public class Passive extends StateMachine.State {
			@Override
			public void entry() {
				ProducerConsumer.Storage storage = Consumer.this
						.assoc(hu.elte.txtuml.examples.producer_consumer.ProducerConsumer.Consumption.storage.class)
						.selectAny();
				Action.unlink(
						ProducerConsumer.Consumption.consumer.class,
						Consumer.this,
						hu.elte.txtuml.examples.producer_consumer.ProducerConsumer.Consumption.storage.class,
						storage);
				Action.delete(Consumer.this);
			}
		}

		@From(ProducerConsumer.Consumer.Init.class)
		@To(ProducerConsumer.Consumer.Active.class)
		public class Initialize extends StateMachine.Transition {
			@Override
			public void effect() {
				Action.send(Consumer.this, new ProducerConsumer.DoWork());
			}
		}

		@From(ProducerConsumer.Consumer.Active.class)
		@To(ProducerConsumer.Consumer.Active.class)
		@Trigger(ProducerConsumer.DoWork.class)
		public class DoRequest extends StateMachine.Transition {
			@Override
			public boolean guard() {
				return (Consumer.this.toConsume > 0);
			}

			@Override
			public void effect() {
				ProducerConsumer.Storage storage = Consumer.this
						.assoc(hu.elte.txtuml.examples.producer_consumer.ProducerConsumer.Consumption.storage.class)
						.selectAny();
				Action.link(
						hu.elte.txtuml.examples.producer_consumer.ProducerConsumer.Request.storage.class,
						storage, ProducerConsumer.Request.consumer.class,
						Consumer.this);
				Action.send(storage, new ProducerConsumer.RequestNotification());
			}
		}

		@From(ProducerConsumer.Consumer.Active.class)
		@To(ProducerConsumer.Consumer.Passive.class)
		@Trigger(ProducerConsumer.DoWork.class)
		public class Stop extends StateMachine.Transition {
			@Override
			public boolean guard() {
				return (Consumer.this.toConsume <= 0);
			}
		}
	}

	public static class OfferNotification extends Signal {
	}

	public static class RequestNotification extends Signal {
	}

	public class Storage extends ModelClass {
		int capacity;

		Storage(final int capacity) {
			this.capacity = capacity;
		}

		public class Init extends StateMachine.Initial {
		}

		public class Working extends StateMachine.State {
		}

		@From(ProducerConsumer.Storage.Init.class)
		@To(ProducerConsumer.Storage.Working.class)
		public class Initialize extends StateMachine.Transition {
		}

		@From(ProducerConsumer.Storage.Working.class)
		@To(ProducerConsumer.Storage.Working.class)
		@Trigger(ProducerConsumer.OfferNotification.class)
		public class CanAccept extends StateMachine.Transition {
			@Override
			public boolean guard() {
				return Storage.this.assoc(ProducerConsumer.InStore.item.class)
						.count() < Storage.this.capacity;
			}

			@Override
			public void effect() {
				ProducerConsumer.Producer producer = Storage.this
						.assoc(hu.elte.txtuml.examples.producer_consumer.ProducerConsumer.Offer.producer.class)
						.selectAny();
				Action.unlink(
						hu.elte.txtuml.examples.producer_consumer.ProducerConsumer.Offer.producer.class,
						producer, ProducerConsumer.Offer.storage.class,
						Storage.this);
				ProducerConsumer.Item item = producer.produce();
				Action.link(
						ProducerConsumer.InStore.storage.class,
						Storage.this,
						hu.elte.txtuml.examples.producer_consumer.ProducerConsumer.InStore.item.class,
						item);
			}
		}

		@From(ProducerConsumer.Storage.Working.class)
		@To(ProducerConsumer.Storage.Working.class)
		@Trigger(ProducerConsumer.OfferNotification.class)
		public class CannotAccept extends StateMachine.Transition {
			@Override
			public boolean guard() {
				return Storage.this.assoc(ProducerConsumer.InStore.item.class)
						.count() >= Storage.this.capacity;
			}

			@Override
			public void effect() {
				Action.send(Storage.this,
						new ProducerConsumer.OfferNotification());
			}
		}

		@From(ProducerConsumer.Storage.Working.class)
		@To(ProducerConsumer.Storage.Working.class)
		@Trigger(ProducerConsumer.RequestNotification.class)
		public class CanServe extends StateMachine.Transition {
			@Override
			public boolean guard() {
				return Storage.this.assoc(ProducerConsumer.InStore.item.class)
						.count() > 0;
			}

			@Override
			public void effect() {
				ProducerConsumer.Consumer consumer = Storage.this
						.assoc(hu.elte.txtuml.examples.producer_consumer.ProducerConsumer.Request.consumer.class)
						.selectAny();
				Action.unlink(
						ProducerConsumer.Request.storage.class,
						Storage.this,
						hu.elte.txtuml.examples.producer_consumer.ProducerConsumer.Request.consumer.class,
						consumer);
				ProducerConsumer.Item item = Storage.this
						.assoc(hu.elte.txtuml.examples.producer_consumer.ProducerConsumer.InStore.item.class)
						.selectAny();
				Action.unlink(
						ProducerConsumer.InStore.storage.class,
						Storage.this,
						hu.elte.txtuml.examples.producer_consumer.ProducerConsumer.InStore.item.class,
						item);
				consumer.consume(item);
			}
		}

		@From(ProducerConsumer.Storage.Working.class)
		@To(ProducerConsumer.Storage.Working.class)
		@Trigger(ProducerConsumer.RequestNotification.class)
		public class CannotServe extends StateMachine.Transition {
			@Override
			public boolean guard() {
				return Storage.this.assoc(ProducerConsumer.InStore.item.class)
						.count() <= 0;
			}

			@Override
			public void effect() {
				Action.send(Storage.this,
						new ProducerConsumer.RequestNotification());
			}
		}
	}

	public class Item extends ModelClass {
	}

	public class Production extends Association {
		public class producer extends
				Association.Many<ProducerConsumer.Producer> {
		}

		public class storage extends Association.One<ProducerConsumer.Storage> {
		}
	}

	public class Offer extends Association {
		public class producer extends
				Association.Many<ProducerConsumer.Producer> {
		}

		public class storage extends
				Association.MaybeOne<ProducerConsumer.Storage> {
		}
	}

	public class Consumption extends Association {
		public class storage extends Association.One<ProducerConsumer.Storage> {
		}

		public class consumer extends
				Association.Many<ProducerConsumer.Consumer> {
		}
	}

	public class Request extends Association {
		public class storage extends
				Association.MaybeOne<ProducerConsumer.Storage> {
		}

		public class consumer extends
				Association.Many<ProducerConsumer.Consumer> {
		}
	}

	public class InStore extends Association {
		public class item extends Association.Many<ProducerConsumer.Item> {
		}

		public class storage extends
				Association.MaybeOne<ProducerConsumer.Storage> {
		}
	}
}

class ProducerConsumerTester {
	  public static void main(final String... args) {
		    ModelExecutor.Settings.setExecutorLog(true);
		    ProducerConsumer.Storage storage = Action.create(ProducerConsumer.Storage.class, Integer.valueOf(2));
		    ProducerConsumer.Producer p1 = Action.create(ProducerConsumer.Producer.class, Integer.valueOf(3));
		    ProducerConsumer.Producer p2 = Action.create(ProducerConsumer.Producer.class, Integer.valueOf(3));
		    ProducerConsumer.Consumer c1 = Action.create(ProducerConsumer.Consumer.class, Integer.valueOf(2));
		    ProducerConsumer.Consumer c2 = Action.create(ProducerConsumer.Consumer.class, Integer.valueOf(2));
		    ProducerConsumer.Consumer c3 = Action.create(ProducerConsumer.Consumer.class, Integer.valueOf(2));
		    Action.link(ProducerConsumer.Production.producer.class, p1, hu.elte.txtuml.examples.producer_consumer.ProducerConsumer.Production.storage.class, storage);
		    Action.link(ProducerConsumer.Production.producer.class, p2, hu.elte.txtuml.examples.producer_consumer.ProducerConsumer.Production.storage.class, storage);
		    Action.link(ProducerConsumer.Consumption.consumer.class, c1, hu.elte.txtuml.examples.producer_consumer.ProducerConsumer.Consumption.storage.class, storage);
		    Action.link(ProducerConsumer.Consumption.consumer.class, c2, hu.elte.txtuml.examples.producer_consumer.ProducerConsumer.Consumption.storage.class, storage);
		    Action.link(ProducerConsumer.Consumption.consumer.class, c3, hu.elte.txtuml.examples.producer_consumer.ProducerConsumer.Consumption.storage.class, storage);
		    Action.start(storage);
		    Action.start(p1);
		    Action.start(p2);
		    Action.start(c1);
		    Action.start(c2);
		    Action.start(c3);
		    ModelExecutor.shutdown();
		  }

}