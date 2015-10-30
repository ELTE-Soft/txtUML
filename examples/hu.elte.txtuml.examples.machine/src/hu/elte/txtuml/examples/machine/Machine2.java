package hu.elte.txtuml.examples.machine;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.ModelExecutor;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import hu.elte.txtuml.examples.machine.Machine2Model.DoYourWork;
import hu.elte.txtuml.examples.machine.Machine2Model.Machine;
import hu.elte.txtuml.examples.machine.Machine2Model.Usage;
import hu.elte.txtuml.examples.machine.Machine2Model.User;

class Machine2Model extends Model {

	// classes
	
	class Machine extends ModelClass {
		int tasksToDo = 3;

		class Init extends Initial {}

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

			class Init extends Initial {}

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

			@From(Init.class) @To(Active.class)
			class Initialize extends Transition {}

			@From(Active.class) @To(Active.class) @Trigger(DoTasks.class)
			class DoActivity extends Transition {
				@Override
				public void effect() {
					DoTasks doTasks = getSignal(DoTasks.class);
					Machine.this.tasksToDo -= doTasks.count;
					Action.log("\tMachine: becoming active...");
				}
			
				@Override
				public boolean guard() {
					return Machine.this.tasksToDo > 0;
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
			public boolean guard() {
				return Machine.this.tasksToDo <= 0;
			}
		}
	}

	class User extends ModelClass {
		
		class Init extends Initial {}
		class Ready extends State {}
		
		@From(Init.class) @To(Ready.class)
		class Initialize extends Transition {
			@Override
			public void effect() {
				Action.log("\tUser: initializing...");				
			}
		}

		@From(Ready.class) @To(Ready.class) @Trigger(DoYourWork.class)
		class Working extends Transition {
			@Override
			public void effect() {
				Action.log("\tUser: working...");
				doWork();
			}
		}
		
		void doWork() {
			Action.log("\tUser: starting to work...");

			Machine myMachine = this.assoc(Usage.usedMachine.class).selectAny();
			
			Action.send(myMachine, new ButtonPress());
			// Switching the machine on.
			
			Action.send(myMachine, new ButtonPress());
			// Trying to switch it off but fails because of the guard.

			Action.send(myMachine, new DoTasks(1));
			// The machine becomes active and decreases its tasks-to-do count
			// by 1.

			Action.send(myMachine, new ButtonPress()); 
			// Trying to switch it off but fails again.
			
			Action.send(myMachine, new DoTasks(1));
			// The machine becomes active again and decreases its tasks-to-do
			// count by 1.

			Action.send(myMachine, new ButtonPress());
			// Trying to switch it off but fails again.

			Action.send(myMachine, new DoTasks(1));
			// This event has no effect, the machine is switched off.

			Action.log("\tUser: work finished...");
		}
	}

	// associations
	
	class Usage extends Association {
		class usedMachine extends One<Machine> {}
		class userOfMachine extends HiddenMany<User> {}
	}

	// signals
	
	class ButtonPress extends Signal {}

	class DoTasks extends Signal {
		int count;
		
		DoTasks(int count) {
			this.count = count;
		}
	}

	static class DoYourWork extends Signal {}
	// Signal classes are allowed to be static for simpler use.
	
}

class MachineTester {

	void test() {
		ModelExecutor.Settings.setExecutorLog(true);

		Machine m = Action.create(Machine.class);
		User u1 = Action.create(User.class);
		User u2 = Action.create(User.class);

		Action.link(Usage.usedMachine.class, m, Usage.userOfMachine.class, u1);
		Action.link(Usage.usedMachine.class, m, Usage.userOfMachine.class, u2);

		Action.log("Machine and users are starting.");
		Action.start(m);
		Action.start(u1);
		Action.start(u2);

		Action.send(u1, new DoYourWork());

		ModelExecutor.shutdown();
	}

}

public class Machine2 {
	public static void main(String[] args) {
		new MachineTester().test();
	}
}