package hu.elte.txtuml.export.uml2.tests.models.send;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.One;

public class A_B extends Association {
	class A_end extends End<One<A>> {
	}

	class B_end extends End<One<B>> {
	}
}