package hu.elte.txtuml.examples.feeder.model;

import hu.elte.txtuml.api.deployment.fmi.FMUEnvironment;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;

public class Source extends ModelClass {
	
	public Source() {
	}
	
	public Source(FMUEnvironment sink) {
		Action.link(SourceEnvAssoc.source.class, this, SourceEnvAssoc.sink.class, sink);
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
	@Trigger(RequestSignal.class)
	class ReceiveResponse extends Transition {
		@Override
		public void effect() {
			RequestSignal trigger = getTrigger(RequestSignal.class);
			for (int i = 0; i < trigger.amount; i++) {
				Action.send(new ResponseSignal(3), assoc(SourceEnvAssoc.sink.class).one());
			}
		}
	}
	
}
