package machine1.x;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.ModelExecutor;
import machine1.x.model.DoYourWork;
import machine1.x.model.Machine;
import machine1.x.model.Usage;
import machine1.x.model.User;

public class Tester {
	static Machine m;

	static void init() {
		m = Action.create(Machine.class);
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
		ModelExecutor.create().setTraceLogging(true).run(Tester::init);
		m.printSwitchOnLog();
	}

}
