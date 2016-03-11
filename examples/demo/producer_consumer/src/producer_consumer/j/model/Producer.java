package producer_consumer.j.model;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.StateMachine;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;

public class Producer extends ModelClass {

	int toProduce;

	Producer(int num) {
		this.toProduce = num;
	}

	Item produce() {
		this.toProduce--;
		Action.send(new DoWork(), this);
		return new Item();
	}

	public class Init extends StateMachine.Initial {
	}

	public class Active extends StateMachine.State {
	}

	public class Passive extends StateMachine.State {

		@Override
		public void entry() {
			Storage storage = Producer.this.assoc(Production.storage.class).selectAny();
			Action.unlink(Production.producer.class, Producer.this, Production.storage.class, storage);
			Action.delete(Producer.this);
		}
	}

	@From(Producer.Init.class) @To(Producer.Active.class)
	public class Initialize extends StateMachine.Transition {

		@Override
		public void effect() {
			Action.send(new DoWork(), Producer.this);
		}
	}

	@From(Producer.Active.class) @To(Producer.Active.class) @Trigger(DoWork.class)
	public class DoOffer extends StateMachine.Transition {

		@Override
		public boolean guard() {
			return 0 < Producer.this.toProduce;
		}

		@Override
		public void effect() {
			Storage storage = Producer.this.assoc(Production.storage.class).selectAny();
			Action.link(Offer.producer.class, Producer.this, Offer.storage.class, storage);
			Action.send(new OfferNotification(), storage);
		}
	}

	@From(Producer.Active.class) @To(Producer.Passive.class) @Trigger(DoWork.class)
	public class Stop extends StateMachine.Transition {

		@Override
		public boolean guard() {
			return Producer.this.toProduce <= 0;
		}
	}
}
