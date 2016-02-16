package hu.elte.txtuml.examples.train.model.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.examples.train.model.Engine;
import hu.elte.txtuml.examples.train.model.Gearbox;

public class GE extends Association {
	public class g extends One<Gearbox> {
	}

	public class e extends One<Engine> {
	}
}
