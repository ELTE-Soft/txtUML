package machine3.j.model.associations;

import hu.elte.txtuml.api.model.Any;
import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.One;
import machine3.j.model.Machine;
import machine3.j.model.User;

public class Usage extends Association {
	public class usedMachine extends End<One<Machine>> {
	}

	public class userOfMachine extends End<Any<User>> {
	}
}
