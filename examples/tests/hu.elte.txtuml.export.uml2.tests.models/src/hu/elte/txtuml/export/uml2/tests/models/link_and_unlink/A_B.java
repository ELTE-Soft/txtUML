package hu.elte.txtuml.export.uml2.tests.models.link_and_unlink;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.One;

public class A_B extends Association {
	class ThisEnd extends End<One<A>> {
	}

	class OtherEnd extends End<One<B>> {
	}
}