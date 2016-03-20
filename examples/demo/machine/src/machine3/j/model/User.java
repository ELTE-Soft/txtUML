package machine3.j.model;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.Collection;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import hu.elte.txtuml.api.stdlib.Timer;
import machine3.j.model.associations.Usage;
import machine3.j.model.signals.ButtonPress;
import machine3.j.model.signals.DoTasks;
import machine3.j.model.signals.DoYourWork;

public class User extends ModelClass {

	public String name;
	public int id;
	int workToDo = 7;

	class Init extends Initial {
	}

	class NotWorking extends State {
	}

	class WhereToGo extends Choice {
	}

	@From(Init.class)
	@To(NotWorking.class)
	class Initialize extends Transition {
		@Override
		public void effect() {
			Action.log("\t" + name.toString() + ": initializing...");
		}
	}

	@From(NotWorking.class)
	@To(WhereToGo.class)
	@Trigger(DoYourWork.class)
	class NW_WTG extends Transition {
	}

	@From(WhereToGo.class)
	@To(NotWorking.class)
	// from choice (trigger is not needed)
	class NoWork extends Transition {
		@Override
		public boolean guard() {
			return workToDo == 0;
		}

		@Override
		public void effect() {
			Action.log("\t" + name.toString() + ": I have no work to do!");
		}
	}

	@From(WhereToGo.class)
	@To(NotWorking.class)
	// from choice (trigger is not needed)
	class HasWork extends Transition {
		@Override
		public boolean guard() {
			return Else();
		}

		@Override
		public void effect() {
			Action.log("\t" + name.toString() + ": I am doing my work now...");
			doWork();
		}
	}

	User findOtherUser(Machine m) {

		Action.log("\t" + name.toString() + ": analyzing machine...");

		Collection<User> usersOfM = m.assoc(Usage.userOfMachine.class);

		Action.log("\t\tI found its users!");
		Action.log("");

		usersOfM.forEach(user -> user.sayHello());
		Action.log("");

		if (usersOfM.contains(this)) {
			Action.log("\t\tI am a user of this machine.");
		}

		if (usersOfM.selectAll(user -> user.id == 0).isEmpty()) {
			Action.log("\t\tNo user of this machine has an id of 0.");
		}

		Collection<User> otherUsers = usersOfM.remove(this);

		if (otherUsers.count() == 1) {
			Action.log("\t\tThere is exactly one other person who is user of this machine.");
		}

		return otherUsers.selectAny();
	}

	void sayHello() {
		Action.log("\t\tHello, I am " + name.toString() + ".");
	}

	void newWork() {
		++workToDo;
		Action.log("\t" + name.toString() + ": I got some new work to do.");
	}

	void workDone() {
		--workToDo;
		Action.log("\t" + name.toString() + ": I am done with some of my work.");
	}

	void doWork() {
		Action.log("\t" + name.toString() + ": starting to work...");

		Machine myMachine = this.assoc(Usage.usedMachine.class).selectAny();

		User otherUser = findOtherUser(myMachine);

		Action.log("\t" + name.toString()
				+ ": giving some of my work to other user...");

		for (int i = 0; i < 3; ++i) {
			Action.log("\t" + name.toString() + ": giving work to other user (" + i
					+ ")");
			otherUser.newWork();
			this.workDone();
		}

		Action.send(new ButtonPress(), myMachine);
		// Switching the machine on.

		Action.log("\t" + name.toString() + ": finishing my work...");

		Action.send(new DoTasks(workToDo), myMachine);

		Timer.start(myMachine, new ButtonPress(), 2000);
		// Switching off the machine with some delay.

		Action.log("\t" + name.toString() + ": work finished...");

	}

}
