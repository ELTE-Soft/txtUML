package hu.elte.txtuml.export.uml2.models.association;

import hu.elte.txtuml.api.model.Association;

public class AB2 extends Association {
	class AEnd extends One<A> {
	}

	class BEnd extends Some<B> {
	}
}