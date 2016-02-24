package hu.elte.txtuml.examples.machine.model1.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.examples.machine.model1.Machine;
import hu.elte.txtuml.examples.machine.model1.User;

public class Usage extends Association {
	public class usedMachine extends One<Machine> {
	}

	public class userOfMachine extends HiddenMany<User> {
	}
}
