package machine2.x;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.LogLevel;
import hu.elte.txtuml.api.model.execution.ModelExecutor;
import machine2.x.model.DoYourWork;
import machine2.x.model.Machine;
import machine2.x.model.Usage;
import machine2.x.model.User;

public class Tester {

	static void init() {
		Machine m = Action.create(Machine.class, 3);
		User u1 = Action.create(User.class);
		User u2 = Action.create(User.class);

		Action.link(Usage.usedMachine.class, m, Usage.userOfMachine.class, u1);
		Action.link(Usage.usedMachine.class, m, Usage.userOfMachine.class, u2);

		Action.log("Machine and users are starting.");
		Action.start(m);
		Action.start(u1);
		Action.start(u2);

		Action.send(new DoYourWork(), u1);
	}

	public static void main(String[] args) {
		ModelExecutor.create().setLogLevel(LogLevel.TRACE).run(Tester::init);
	}

}
