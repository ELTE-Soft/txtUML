package hu.elte.txtuml.examples.machine;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelExecutor;
import hu.elte.txtuml.examples.machine.xmodel1.Machine;
import hu.elte.txtuml.examples.machine.xmodel1.User;
import hu.elte.txtuml.examples.machine.xmodel1.Usage;
import hu.elte.txtuml.examples.machine.xmodel1.DoYourWork;

public class XMachine1Tester {

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

		Action.send(new DoYourWork(), u1);

		ModelExecutor.shutdown();
	}

	public static void main(String[] args) {
		new XMachine1Tester().test();
	}

}
