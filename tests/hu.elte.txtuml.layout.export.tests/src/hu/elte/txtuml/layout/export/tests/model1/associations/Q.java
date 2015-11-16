package hu.elte.txtuml.layout.export.tests.model1.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.layout.export.tests.model1.A;
import hu.elte.txtuml.layout.export.tests.model1.B;

public class Q extends Association {
	public class e1 extends Many<A> {
	}

	public class e2 extends One<B> {
	}
}
