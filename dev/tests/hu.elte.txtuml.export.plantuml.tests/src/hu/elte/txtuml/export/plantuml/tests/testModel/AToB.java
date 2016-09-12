package hu.elte.txtuml.export.plantuml.tests.testModel;

import hu.elte.txtuml.api.model.Association;

public class AToB extends Association {
	public class ASide extends One<A> {
	}

	public class BSide extends One<B> {
	}
}
