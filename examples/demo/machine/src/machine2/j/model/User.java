package machine2.j.model;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import machine2.j.model.associations.Usage;
import machine2.j.model.signals.ButtonPress;
import machine2.j.model.signals.DoTasks;
import machine2.j.model.signals.DoYourWork;

public class User extends ModelClass {

	public class Init extends Initial {
	}

	public class Ready extends State {
	}

	@From(Init.class)
	@To(Ready.class)
	class Initialize extends Transition {
		@Override
		public void effect() {
			Action.log("\tUser: initializing...");
		}
	}

	@From(Ready.class)
	@To(Ready.class)
	@Trigger(DoYourWork.class)
	class Working extends Transition {
		@Override
		public void effect() {
			Action.log("\tUser: working...");
			doWork();
		}
	}

	void doWork() {
		Action.log("\tUser: starting to work...");

		Machine myMachine = this.assoc(Usage.usedMachine.class).one();

		Action.send(new ButtonPress(), myMachine);
		// Switching the machine on.

		Action.send(new ButtonPress(), myMachine);
		// Trying to switch it off but fails because of the guard.

		Action.send(new DoTasks(1), myMachine);
		// The machine becomes active and decreases its tasks-to-do count
		// by 1.

		Action.send(new ButtonPress(), myMachine);
		// Trying to switch it off but fails again.

		Action.send(new DoTasks(1), myMachine);
		// The machine becomes active again and decreases its tasks-to-do
		// count by 1.

		Action.send(new ButtonPress(), myMachine);
		// Trying to switch it off but fails again.

		Action.send(new DoTasks(1), myMachine);
		// This event has no effect, the machine is switched off.

		Action.log("\tUser: work finished...");
	}
}
