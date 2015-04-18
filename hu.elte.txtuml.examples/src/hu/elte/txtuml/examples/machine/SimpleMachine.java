package hu.elte.txtuml.examples.machine;

import hu.elte.txtuml.api.*;
import hu.elte.txtuml.examples.machine.MachineModel.*;

class MachineModel extends Model {

	class Machine extends ModelClass {
		ModelInt tasksTodo = new ModelInt(2);

		class Init extends Initial {}

		class Off extends State {
			@Override
			public void entry() {
				Action.log("Machine enters state: 'off'");
			}

			@Override
			public void exit() {
				Action.log("Machine exits state: 'off'");
			}
		}

		class On extends CompositeState {
			@Override
			public void entry() {
				Action.log("Machine enters state: 'on'");
			}

			@Override
			public void exit() {
				Action.log("Machine exits state: 'on'");
			}

			class Init extends Initial {}

			class Active extends State {
				@Override
				public void entry() {
					Action.log("Machine enters state: 'active'");
					Action.log("tasks to do: " + Machine.this.tasksTodo);
				}

				@Override
				public void exit() {
					Action.log("Machine exits state: 'active'");
				}
			}

			@From(Init.class) @To(Active.class)
			class Initialize extends Transition {}

			@From(Active.class) @To(Active.class) @Trigger(DoTasks.class)
			class DoActivity extends Transition {
				@Override
				public void effect() {
					DoTasks doTasks = getSignal(DoTasks.class);
					Machine.this.tasksTodo = Machine.this.tasksTodo
							.subtract(doTasks.count);
					Action.log("\tMachine: becoming active...");
				}
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

			@Override
			public ModelBool guard() {
				return Machine.this.tasksTodo.isLessEqual(new ModelInt(0));
			}
		}
	}

	class User extends ModelClass {
		Machine doWork(User param) {
			Action.log("User: starting to work...");

			Machine myMachine = this.assoc(Usage.usedMachine.class).selectAny();

			Action.send(myMachine, new ButtonPress());
			// Switching the machine on.
			
			Action.send(myMachine, new ButtonPress());
			// Trying to switch it off but fails because of the guard.

			Action.send(myMachine, new DoTasks(new ModelInt(1)));
			// The machine becomes active and decreases its tasks-to-do count
			// by 1.

			Action.send(myMachine, new ButtonPress()); 
			// Trying to switch it off but fails again.
			
			Action.send(myMachine, new DoTasks(new ModelInt(1)));
			// The machine becomes active again and decreases its tasks-to-do
			// count by 1.

			Action.send(myMachine, new ButtonPress());
			// Trying to switch it off but fails again.

			Action.send(myMachine, new DoTasks(new ModelInt(1)));
			// This event has no effect, the machine is switched off.

			Action.log("User: work finished...");
			return myMachine;
		}
	}

	class Usage extends Association {
		class usedMachine extends One<Machine> {}
		class userOfMachine extends Many<User> {}
	}

	class ButtonPress extends Signal {
		ModelString name = new ModelString("ButtonPress");
	}

	class DoTasks extends Signal {
		ModelInt count;
		
		DoTasks(ModelInt count) {
			this.count = count;
		}
	}

	class SpecialDoTasks extends DoTasks {

		SpecialDoTasks(ModelInt count) {
			super(count);
		}
	}

}

class MachineTester {
	
	void test() {
		ModelExecutor.Settings.setExecutorLog(true);

		Machine m = Action.create(Machine.class);

		User u1 = Action.create(User.class);
		User u2 = Action.create(User.class);

		Action.link(Usage.usedMachine.class, m, Usage.userOfMachine.class, u1);
		Action.link(Usage.usedMachine.class, m, Usage.userOfMachine.class, u2);

		Action.start(m);

		u1.doWork(u2);
	}
	
}

public class SimpleMachine {
	public static void main(String[] args) {
		new MachineTester().test();
	}
}