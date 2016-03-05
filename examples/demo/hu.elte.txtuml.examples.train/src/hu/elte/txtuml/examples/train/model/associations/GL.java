package hu.elte.txtuml.examples.train.model.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.examples.train.model.Gearbox;
import hu.elte.txtuml.examples.train.model.Lamp;

public class GL extends Association {
	public class g extends One<Gearbox> {
	}

	public class l extends One<Lamp> {
	}
}
