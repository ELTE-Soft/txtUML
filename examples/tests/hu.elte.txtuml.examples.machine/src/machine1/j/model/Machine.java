package machine1.j.model;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import machine1.j.model.signals.ButtonPress;

public class Machine extends ModelClass {
	public class Init extends Initial {}

	public class Off extends State {
		@Override
		public void entry() {
			Action.log("\tMachine enters state: 'off'");
		}

		@Override
		public void exit() {
			Action.log("\tMachine exits state: 'off'");
		}
	}

	public class On extends State {
		@Override
		public void entry() {
			Action.log("\tMachine enters state: 'on'");
		}

		@Override
		public void exit() {
			Action.log("\tMachine exits state: 'on'");
		}

	}

	@From(Init.class) @To(Off.class)
	class Initialize extends Transition {
		@Override
		public void effect() {
			Action.log("\tMachine: initializing...");
		}
	}

	@From(Off.class) @To(On.class) @Trigger(ButtonPress.class)
	class SwitchOn extends Transition {
		@Override
		public void effect() {
			Action.log("\tMachine: switching on...");
		}

	}

	@From(On.class) @To(Off.class) @Trigger(ButtonPress.class)
	class SwitchOff extends Transition {
		@Override
		public void effect() {
			Action.log("\tMachine: switching off...");
		}
	}

}
