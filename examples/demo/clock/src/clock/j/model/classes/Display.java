package clock.j.model.classes;

import clock.j.model.interfaces.ValueIfc;
import clock.j.model.signals.HandValue;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.BehaviorPort;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.InPort;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;

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
			hour = getTrigger(HandValue.class).value;
			minute = 0;
			second = 0;
			refresh();
		}
	}

	@From(Accepting.class) @To(Accepting.class) @Trigger(port = MinutePort.class, value = HandValue.class)
	class MinuteChanged extends Transition {
		public void effect() {
			minute = getTrigger(HandValue.class).value;
			second = 0;
			if(minute > 0) {
				refresh();
			}
		}
	}

	@From(Accepting.class) @To(Accepting.class) @Trigger(port = SecondPort.class, value = HandValue.class)
	class SecondChanged extends Transition {
		public void effect() {
			second = getTrigger(HandValue.class).value;
			if(second > 0) {
				refresh();
			}
		}
	}
}
