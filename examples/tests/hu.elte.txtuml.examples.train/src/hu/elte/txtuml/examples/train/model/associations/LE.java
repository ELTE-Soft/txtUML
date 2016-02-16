package hu.elte.txtuml.examples.train.model.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.examples.train.model.Engine;
import hu.elte.txtuml.examples.train.model.Lamp;

public class LE extends Association {
	public class l extends One<Lamp> {
	}

	public class e extends One<Engine> {
	}
}
