package clock.j.model.classes;

import clock.j.model.associations.TimerOfPendulum;
import clock.j.model.interfaces.TickIfc;
import clock.j.model.signals.Tick;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import hu.elte.txtuml.api.stdlib.timers.Timer;

public class Pendulum extends ModelClass {

	private int unit = 1000;
	
	public class OutTickPort extends OutPort<TickIfc> {}
	
	public class Init extends Initial {}
	public class Working extends State {
		public void entry() {
			Action.send(new Tick(), port(OutTickPort.class).required::reception);
		}
	}
	
	@From(Init.class) @To(Working.class)
	class Initialize extends Transition {
		public void effect() {
			Timer timer = Timer.start(Pendulum.this, new Tick(), unit);
			Action.link(TimerOfPendulum.timer.class, timer, TimerOfPendulum.pendulum.class, Pendulum.this);
		}
	}
	
	@From(Working.class) @To(Working.class) @Trigger(Tick.class)
	class DoTick extends Transition {
		public void effect() {
			assoc(TimerOfPendulum.timer.class).selectAny().reset(unit);
		}
	}
}
