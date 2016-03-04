package hu.elte.txtuml.examples.machine;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelExecutor;
import hu.elte.txtuml.api.stdlib.Timer;
import hu.elte.txtuml.examples.machine.model3.Machine;
import hu.elte.txtuml.examples.machine.model3.User;
import hu.elte.txtuml.examples.machine.model3.associations.Usage;
import hu.elte.txtuml.examples.machine.model3.signals.DoYourWork;

public class Machine3Tester {

	void test() {
		ModelExecutor.Settings.setExecutorLog(true);

		Machine m = Action.create(Machine.class, 3);
		User u1 = Action.create(User.class);
		User u2 = Action.create(User.class);

		u1.name = "user1";
		u2.name = "user2";
		u1.id = 1;
		u2.id = 2;

		Action.link(Usage.usedMachine.class, m, Usage.userOfMachine.class, u1);
		Action.link(Usage.usedMachine.class, m, Usage.userOfMachine.class, u2);

		Action.log("Machine and users are starting.");
		Action.start(m);
		Action.start(u1);
		Action.start(u2);

		Action.log("One of the users is starting to do his or her work.");

		User oneOfTheUsers = m.assoc(Usage.userOfMachine.class).selectAny();
		// In Machine1 and Machine2 models this cannot be done as userOfMachine
		// association end is non-navigable in that model.
		Action.send(new DoYourWork(), oneOfTheUsers);

		Timer.start(oneOfTheUsers, new DoYourWork(), 5000);

		Timer.shutdown();
	}

	public static void main(String[] args) {
		new Machine3Tester().test();
	}

}
