package hu.elte.txtuml.export.uml2.tests.models.association;

import hu.elte.txtuml.api.model.Association;

public class AB1 extends Association {
	class AEnd extends MaybeOne<A> {
	}

	class BEnd extends Many<B> {
	}
}