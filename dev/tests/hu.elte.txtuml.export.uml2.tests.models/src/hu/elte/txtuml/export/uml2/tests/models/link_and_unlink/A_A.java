package hu.elte.txtuml.export.uml2.tests.models.link_and_unlink;

import hu.elte.txtuml.api.model.Association;

public class A_A extends Association {
	class ThisEnd extends One<A> {
	}

	class OtherEnd extends One<A> {
	}
}