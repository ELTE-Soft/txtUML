package hu.elte.txtuml.export.uml2.tests.models.send;

import hu.elte.txtuml.api.model.Association;

public class A_B extends Association {
	class A_end extends One<A> {
	}

	class B_end extends One<B> {
	}
}