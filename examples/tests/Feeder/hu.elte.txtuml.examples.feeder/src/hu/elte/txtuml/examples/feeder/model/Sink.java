package hu.elte.txtuml.examples.feeder.model;

import hu.elte.txtuml.api.deployment.fmi.FMUEnvironment;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;

public class Sink extends ModelClass {

	int requested = 0;
	int remaining = 0;

	public Sink() {
	}

	public Sink(FMUEnvironment source) {
		Action.link(SinkEnvAssoc.sink.class, this, SinkEnvAssoc.source.class, source);
	}

	class Init extends Initial {
	}

	class Running extends State {
	}

	@From(Init.class)
	@To(Running.class)
	class InitToRunning extends Transition {
	}

	@From(Running.class)
	@To(Running.class)
	@Trigger(ResponseSignal.class)
	class ReceivedLastResponse extends Transition {
		
		@Override
		public boolean guard() {
			return remaining <= 1;
		}
		
		@Override
		public void effect() {
			ResponseSignal trigger = getTrigger(ResponseSignal.class);
			Action.log("" + trigger.data);
			Action.log("RECEIVED " + requested + " MESSAGES");
			++requested;
			remaining = requested;
			Action.send(new RequestSignal(requested), assoc(SinkEnvAssoc.source.class).one());
		}
	}

	@From(Running.class)
	@To(Running.class)
	@Trigger(ResponseSignal.class)
	class ReceiveResponse extends Transition {
		
		@Override
		public boolean guard() {
			return remaining > 1;
		}
		
		@Override
		public void effect() {
			ResponseSignal trigger = getTrigger(ResponseSignal.class);
			Action.log("" + trigger.data);
			--remaining;
		}
	}

	public void start() {
		++requested;
		remaining = requested;
		Action.send(new RequestSignal(requested), assoc(SinkEnvAssoc.source.class).one());
	}

}
