package hu.elte.txtuml.examples.machine;

import hu.elte.txtuml.api.model.*;
import hu.elte.txtuml.examples.machine.Machine1Model.*;

class Machine1Model extends Model {

	// classes
	
	class Machine extends ModelClass {

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

		class On extends State {
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
			Action.send(myMachine, new ButtonPress());
			Action.send(myMachine, new ButtonPress());
			Action.log("\tUser: work finished...");
		}
	}

	// associations
	
	class Usage extends Association {
		class usedMachine extends One<Machine> {}
		class userOfMachine extends HiddenMany<User> {}
	}

	// signals
	
	static class ButtonPress extends Signal {}

	static class DoYourWork extends Signal {}
	// Signal classes are allowed to be static for simpler use.
	
}

class Machine1Tester {

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

public class Machine1 {
	public static void main(String[] args) {
		new Machine1Tester().test();
	}
}
