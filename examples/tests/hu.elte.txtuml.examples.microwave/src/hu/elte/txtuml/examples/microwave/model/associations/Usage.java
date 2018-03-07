package hu.elte.txtuml.examples.microwave.model.associations;

import hu.elte.txtuml.api.model.Any;
import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.One;
import hu.elte.txtuml.examples.microwave.model.Human;
import hu.elte.txtuml.examples.microwave.model.Microwave;

public class Usage extends Association {
	public class usedMicrowave extends End<One<Microwave>> {
	}

	public class userOfMicrowave extends End<Any<Human>> {
	}
}