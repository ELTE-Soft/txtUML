package hu.elte.txtuml.examples.clock.model.classes;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.BehaviorPort;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.InPort;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import hu.elte.txtuml.examples.clock.model.interfaces.ValueIfc;
import hu.elte.txtuml.examples.clock.model.signals.HandValue;

public class Display extends ModelClass {
	int hour;
	int minute;
	int second;
	
	@BehaviorPort
	public class HourPort extends InPort<ValueIfc> {}

	@BehaviorPort
	public class MinutePort extends InPort<ValueIfc> {}

	@BehaviorPort
	public class SecondPort extends InPort<ValueIfc> {}
	
	public Display(int hour, int minute, int second) {
		this.hour = hour;
		this.minute = minute;
		this.second = second;
	}
	
	private void refresh() {
		Action.log("" + hour + ":" + minute + ":" + second);
	}
	
	class Init extends Initial {}
	class Accepting extends State {}
	
	@From(Init.class) @To(Accepting.class)
	class Initialize extends Transition {}
	
	@From(Accepting.class) @To(Accepting.class) @Trigger(port = HourPort.class, value = HandValue.class)
	class HourChanged extends Transition {
		public void effect() {
			hour = ((HandValue)getSignal()).value;
			minute = 0;
			second = 0;
			refresh();
		}
	}

	@From(Accepting.class) @To(Accepting.class) @Trigger(port = MinutePort.class, value = HandValue.class)
	class MinuteChanged extends Transition {
		public void effect() {
			minute = ((HandValue)getSignal()).value;
			second = 0;
			if(minute > 0) {
				refresh();
			}
		}
	}

	@From(Accepting.class) @To(Accepting.class) @Trigger(port = SecondPort.class, value = HandValue.class)
	class SecondChanged extends Transition {
		public void effect() {
			second = ((HandValue)getSignal()).value;
			if(second > 0) {
				refresh();
			}
		}
	}
}
