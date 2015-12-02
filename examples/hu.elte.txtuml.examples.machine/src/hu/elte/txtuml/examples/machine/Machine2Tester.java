package hu.elte.txtuml.examples.machine;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelExecutor;
import hu.elte.txtuml.examples.machine.model2.Machine;
import hu.elte.txtuml.examples.machine.model2.User;
import hu.elte.txtuml.examples.machine.model2.associations.Usage;
import hu.elte.txtuml.examples.machine.model2.signals.DoYourWork;

public class Machine2Tester {

	void test() {
		ModelExecutor.Settings.setExecutorLog(true);

		Machine m = Action.create(Machine.class, 3);
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

	public static void main(String[] args) {
		new Machine2Tester().test();
	}

}
