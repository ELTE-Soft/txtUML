package hu.elte.txtuml.examples.microwave.model.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.examples.microwave.model.Human;
import hu.elte.txtuml.examples.microwave.model.Microwave;

public class Usage extends Association {
	public class usedMicrowave extends One<Microwave> {
	}

	public class userOfMicrowave extends Many<Human> {
	}
}