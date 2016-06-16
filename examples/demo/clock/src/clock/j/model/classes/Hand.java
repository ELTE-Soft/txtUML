package clock.j.model.classes;

import clock.j.model.interfaces.TickIfc;
import clock.j.model.interfaces.ValueIfc;
import clock.j.model.signals.HandValue;
import clock.j.model.signals.Tick;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.BehaviorPort;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;

public class Hand extends ModelClass {
	private int maxValue;
	private int currentValue;
	
	@BehaviorPort
	public class InTickPort extends InPort<TickIfc> {}

	public class OutTickPort extends OutPort<TickIfc> {}
	public class ValuePort extends OutPort<ValueIfc> {}
	
	public Hand(int maxValue, int currentValue) {
		this.maxValue = maxValue;
		this.currentValue = currentValue;
	}
	
	public class Init extends Initial {}
	public class Working extends State {}

	@From(Init.class) @To(Working.class)
	class Initialize extends Transition {}
	
	@From(Working.class) @To(Working.class) @Trigger(Tick.class)
	class DoWork extends Transition {
		public void effect() {
			++currentValue;
			if(currentValue == maxValue) {
				currentValue = 0;
				Action.send(new Tick(), port(OutTickPort.class).required::reception);
			}
			Action.send(new HandValue(currentValue), port(ValuePort.class).required::reception);
		}
	}
}
