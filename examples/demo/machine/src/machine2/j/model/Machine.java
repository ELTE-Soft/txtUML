package machine2.j.model;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import machine2.j.model.signals.ButtonPress;
import machine2.j.model.signals.DoTasks;

public class Machine extends ModelClass {
	int tasksToDo;

	Machine(int tasksToDo) {
		this.tasksToDo = tasksToDo;
	}

	class Init extends Initial {
	}

	class Off extends State {
		@Override
		public void entry() {
			Action.log("\tMachine enters state: 'off'");
		}

		@Override
		public void exit() {
			Action.log("\tMachine exits state: 'off'");
		}
	}

	class On extends CompositeState {
		@Override
		public void entry() {
			Action.log("\tMachine enters state: 'on'");
		}

		@Override
		public void exit() {
			Action.log("\tMachine exits state: 'on'");
		}

		class Init extends Initial {
		}

		class Active extends State {
			@Override
			public void entry() {
				Action.log("\tMachine enters state: 'active'");
				Action.log("\tMachine: tasks to do: " + Machine.this.tasksToDo);
			}

			@Override
			public void exit() {
				Action.log("\tMachine exits state: 'active'");
			}
		}

		@From(Init.class)
		@To(Active.class)
		class Initialize extends Transition {
		}

		@From(Active.class)
		@To(Active.class)
		@Trigger(DoTasks.class)
		class DoActivity extends Transition {
			@Override
			public void effect() {
				DoTasks doTasks = getSignal();
				Machine.this.tasksToDo -= doTasks.count;
				Action.log("\tMachine: becoming active...");
			}

			@Override
			public boolean guard() {
				return Machine.this.tasksToDo > 0;
			}
		}
	}

	@From(Init.class)
	@To(Off.class)
	class Initialize extends Transition {
		@Override
		public void effect() {
			Action.log("\tMachine: initializing...");
		}
	}

	@From(Off.class)
	@To(On.class)
	@Trigger(ButtonPress.class)
	class SwitchOn extends Transition {
		@Override
		public void effect() {
			Action.log("\tMachine: switching on...");
		}

	}

	@From(On.class)
	@To(Off.class)
	@Trigger(ButtonPress.class)
	class SwitchOff extends Transition {
		@Override
		public void effect() {
			Action.log("\tMachine: switching off...");
		}

		@Override
		public boolean guard() {
			return Machine.this.tasksToDo <= 0;
		}
	}
}
