package hu.elte.txtuml.examples.clock.model.classes;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.OutPort;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import hu.elte.txtuml.api.stdlib.Timer;
import hu.elte.txtuml.examples.clock.model.interfaces.TickIfc;
import hu.elte.txtuml.examples.clock.model.signals.Tick;

public class Pendulum extends ModelClass {
	private Timer.Handle timerHandle;
	private int unit = 1000;
	
	public class OutTickPort extends OutPort<TickIfc> {}
	
	class Init extends Initial {}
	class Working extends State {
		public void entry() {
			Action.send(new Tick(), port(OutTickPort.class).required::reception);
		}
	}
	
	@From(Init.class) @To(Working.class)
	class Initialize extends Transition {
		public void effect() {
			timerHandle = Timer.start(Pendulum.this, new Tick(), unit);
		}
	}
	
	@From(Working.class) @To(Working.class) @Trigger(Tick.class)
	class DoTick extends Transition {
		public void effect() {
			timerHandle.reset(unit);
		}
	}
}
