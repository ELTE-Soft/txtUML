package hu.elte.txtuml.examples.clock.model.classes;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.BehaviorPort;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.Interface;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Port;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import hu.elte.txtuml.examples.clock.model.interfaces.TickIfc;
import hu.elte.txtuml.examples.clock.model.interfaces.ValueIfc;
import hu.elte.txtuml.examples.clock.model.signals.HandValue;
import hu.elte.txtuml.examples.clock.model.signals.Tick;

public class Hand extends ModelClass {
	private int maxValue;
	private int currentValue;
	
	@BehaviorPort
	public class InTickPort extends Port<TickIfc, Interface.Empty> {}

	public class OutTickPort extends Port<Interface.Empty, TickIfc> {}
	public class ValuePort extends Port<Interface.Empty, ValueIfc> {}
	
	public Hand(int maxValue, int currentValue) {
		this.maxValue = maxValue;
		this.currentValue = currentValue;
	}
	
	class Init extends Initial {}
	class Working extends State {}

	@From(Init.class) @To(Working.class)
	class Initialize extends Transition {}
	
	@From(Working.class) @To(Working.class) @Trigger(Tick.class)
	class DoWork extends Transition {
		public void effect() {
			++currentValue;
			if(currentValue == maxValue) {
				currentValue = 0;
				Action.send(port(OutTickPort.class).provided::reception, new Tick());
			}
			Action.send(port(ValuePort.class).provided::reception, new HandValue(currentValue));
		}
	}
}
