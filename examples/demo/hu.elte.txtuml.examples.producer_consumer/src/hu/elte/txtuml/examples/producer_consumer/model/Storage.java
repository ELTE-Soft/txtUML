package hu.elte.txtuml.examples.producer_consumer.model;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.StateMachine;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;

public class Storage extends ModelClass {

	int capacity;

	Storage(int capacity) {
		this.capacity = capacity;
	}

	public class Init extends StateMachine.Initial {
	}

	public class Working extends StateMachine.State {
	}

	@From(Storage.Init.class) @To(Storage.Working.class)
	public class Initialize extends StateMachine.Transition {
	}

	@From(Storage.Working.class) @To(Storage.Working.class) @Trigger(OfferNotification.class)
	public class CanAccept extends StateMachine.Transition {

		@Override
		public boolean guard() {
			int count = Storage.this.assoc(InStore.item.class).count();
			return count < Storage.this.capacity;
		}

		@Override
		public void effect() {
			Producer producer = Storage.this.assoc(Offer.producer.class).selectAny();
			Action.unlink(Offer.producer.class, producer, Offer.storage.class, Storage.this);
			Item item = producer.produce();
			Action.link(InStore.storage.class, Storage.this, InStore.item.class, item);
		}
	}

	@From(Storage.Working.class) @To(Storage.Working.class) @Trigger(OfferNotification.class)
	public class CannotAccept extends StateMachine.Transition {

		@Override
		public boolean guard() {
			int count = Storage.this.assoc(InStore.item.class).count();
			return count >= Storage.this.capacity;
		}

		@Override
		public void effect() {
			Action.send(new OfferNotification(), Storage.this);
		}
	}

	@From(Storage.Working.class) @To(Storage.Working.class) @Trigger(RequestNotification.class)
	public class CanServe extends StateMachine.Transition {

		@Override
		public boolean guard() {
			int count = Storage.this.assoc(InStore.item.class).count();
			return count > 0;
		}

		@Override
		public void effect() {
			Consumer consumer = Storage.this.assoc(Request.consumer.class).selectAny();
			Action.unlink(Request.storage.class, Storage.this, Request.consumer.class, consumer);
			Item item = Storage.this.assoc(InStore.item.class).selectAny();
			Action.unlink(InStore.storage.class, Storage.this, InStore.item.class, item);
			consumer.consume(item);
		}
	}

	@From(Storage.Working.class) @To(Storage.Working.class) @Trigger(RequestNotification.class)
	public class CannotServe extends StateMachine.Transition {

		@Override
		public boolean guard() {
			int count = Storage.this.assoc(InStore.item.class).count();
			return count <= 0;
		}

		@Override
		public void effect() {
			Action.send(new RequestNotification(), Storage.this);
		}
	}
}
