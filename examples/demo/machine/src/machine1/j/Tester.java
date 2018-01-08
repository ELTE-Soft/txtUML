package machine1.j;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.LogLevel;
import hu.elte.txtuml.api.model.execution.ModelExecutor;
import machine1.j.model.Machine;
import machine1.j.model.User;
import machine1.j.model.associations.Usage;
import machine1.j.model.signals.DoYourWork;

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
		ModelExecutor.create().setLogLevel(LogLevel.TRACE).run(Tester::init);
		m.printSwitchOnLog();
	}
}
