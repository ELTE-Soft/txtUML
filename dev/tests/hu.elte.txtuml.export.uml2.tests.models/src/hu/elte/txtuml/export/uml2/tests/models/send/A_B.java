package hu.elte.txtuml.export.uml2.tests.models.send;

import hu.elte.txtuml.api.model.Association;

public class A_B extends Association {
	class ThisEnd extends One<A> {
	}

	class OtherEnd extends One<B> {
	}
}