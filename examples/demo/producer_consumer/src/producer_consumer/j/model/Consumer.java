package producer_consumer.j.model;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.StateMachine;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;

public class Consumer extends ModelClass {

	int toConsume;

	Consumer(int toConsume) {
		this.toConsume = toConsume;
	}

	void consume(Item item) {
		this.toConsume--;
		Action.send(new DoWork(), this);
	}

	public class Init extends StateMachine.Initial {
	}

	public class Active extends StateMachine.State {
	}

	public class Passive extends StateMachine.State {

		@Override
		public void entry() {
			Storage storage = Consumer.this.assoc(Consumption.storage.class).one();
			Action.unlink(Consumption.consumer.class, Consumer.this, Consumption.storage.class, storage);
			Action.delete(Consumer.this);
		}
	}

	@From(Consumer.Init.class) @To(Consumer.Active.class)
	public class Initialize extends StateMachine.Transition {

		@Override
		public void effect() {
			Action.send(new DoWork(), Consumer.this);
		}
	}

	@From(Consumer.Active.class) @To(Consumer.Active.class) @Trigger(DoWork.class)
	public class DoRequest extends StateMachine.Transition {

		@Override
		public boolean guard() {
			return Consumer.this.toConsume > 0;
		}

		@Override
		public void effect() {
			Storage storage = Consumer.this.assoc(Consumption.storage.class).one();
			Action.link(Request.storage.class, storage, Request.consumer.class, Consumer.this);
			Action.send(new RequestNotification(), storage);
		}
	}

	@From(Consumer.Active.class) @To(Consumer.Passive.class) @Trigger(DoWork.class)
	public class Stop extends StateMachine.Transition {

		@Override
		public boolean guard() {
			return Consumer.this.toConsume <= 0;
		}
	}
}
