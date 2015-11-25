package hu.elte.txtuml.examples.machine.model2.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.examples.machine.model2.Machine;
import hu.elte.txtuml.examples.machine.model2.User;

public class Usage extends Association {
	public class usedMachine extends One<Machine> {
	}

	public class userOfMachine extends HiddenMany<User> {
	}
}
