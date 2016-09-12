package hu.elte.txtuml.export.plantuml.tests.testModel;

import hu.elte.txtuml.api.model.Association;

public class BToC extends Association {
	public class BSide extends One<B> {
	}

	public class CSide extends One<C> {
	}
}
