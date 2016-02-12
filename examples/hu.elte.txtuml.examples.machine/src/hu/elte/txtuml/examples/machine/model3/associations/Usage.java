package hu.elte.txtuml.examples.machine.model3.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.examples.machine.model3.Machine;
import hu.elte.txtuml.examples.machine.model3.User;

public class Usage extends Association {
	public class usedMachine extends One<Machine> {
	}

	public class userOfMachine extends Many<User> {
	}
}
