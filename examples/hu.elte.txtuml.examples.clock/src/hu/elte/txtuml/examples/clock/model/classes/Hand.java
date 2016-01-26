package hu.elte.txtuml.examples.clock.model.classes;

import hu.elte.txtuml.examples.clock.model.interfaces.EmptyIfc;
import hu.elte.txtuml.examples.clock.model.interfaces.TickIfc;
import hu.elte.txtuml.examples.clock.model.signals.Tick;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.BehaviorPort;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Port;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;

public class Hand extends ModelClass {
	private int maxValue;
	private int currentValue;
	private int index;
	
	@BehaviorPort
	public class InTickPort extends Port<EmptyIfc,TickIfc> {}

	public class OutTickPort extends Port<TickIfc,EmptyIfc> {}
	
	public Hand(int maxValue, int index) {
		this.maxValue = maxValue;
		this.currentValue = 0;
		this.index = index;
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
			logValue();
		}
		
	}
	
	private void logValue() {
		String log = "";
		for(int i=0; i<index; ++i) {
			log += "  :";
		}
		Action.log(log + currentValue);
	}
}
