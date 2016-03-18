package machine1.j.model.associations;

import hu.elte.txtuml.api.model.Association;
import machine1.j.model.Machine;
import machine1.j.model.User;

public class Usage extends Association {
	public class usedMachine extends One<Machine> {
	}

	public class userOfMachine extends HiddenMany<User> {
	}
}
