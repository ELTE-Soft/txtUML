package machine1.j.model;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.Collection;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import machine1.j.model.associations.Usage;
import machine1.j.model.signals.ButtonPress;
import machine1.j.model.signals.DoYourWork;

public class User extends ModelClass {

	boolean b;
	
	class Init extends Initial {
	}

	class Ready extends State {
	}

	@From(Init.class)
	@To(Ready.class)
	class Initialize extends Transition {
		@Override
		public void effect() {
			boolean x;
			x = b;
			b = x;
			x &= b;
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
			doWork(null);
		}
		
		@Override
		public boolean guard() {
			return true;
		}
	}

	void doWork(Collection<Integer> c) {
		if (b) {
			Action.log(("\tUser: starting to work..."));
		}
		Machine myMachine = this.assoc(Usage.usedMachine.class).selectAny();
		if (b) {
			Action.send(new ButtonPress(), myMachine);
		} else {
			Action.send(new ButtonPress(), myMachine);
		}
		while (b) {
			Action.send(new ButtonPress(), myMachine);
		}
		do {
			Action.send(new ButtonPress(), myMachine);
		} while (b);
		for (int i : c) {
			Action.log("\tUser: work finished...");
		}
		Action.log("\tUser: work finished...");
	}

}
