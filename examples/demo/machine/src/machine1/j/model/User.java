package machine1.j.model;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import machine1.j.model.associations.Usage;
import machine1.j.model.signals.ButtonPress;
import machine1.j.model.signals.DoYourWork;

public class User extends ModelClass {

	class Init extends Initial {
	}

	class Ready extends State {
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
		Machine myMachine = this.assoc(Usage.usedMachine.class).selectAny();
		Action.send(new ButtonPress(), myMachine);
		Action.send(new ButtonPress(), myMachine);
		Action.send(new ButtonPress(), myMachine);
		Action.log("\tUser: work finished...");
	}

}
