package hu.elte.txtuml.export.plantuml.tests.models.testmodel;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.One;

public class BToC extends Association {
	public class BSide extends End<One<B>> {
	}

	public class CSide extends End<One<C>> {
	}
}
