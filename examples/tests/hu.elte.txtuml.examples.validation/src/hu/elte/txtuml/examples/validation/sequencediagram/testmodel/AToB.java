package hu.elte.txtuml.examples.validation.sequencediagram.testmodel;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.One;

public class AToB extends Association {
	public class ASide extends End<One<A>> {
	}

	public class BSide extends End<One<B>> {
	}
}
