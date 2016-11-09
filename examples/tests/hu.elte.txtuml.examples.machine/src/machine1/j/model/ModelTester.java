package machine1.j.model;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelClass;
import machine1.j.model.associations.Usage;
import machine1.j.model.signals.DoYourWork;

public class ModelTester extends ModelClass {
	public void test () {
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
	}
}
