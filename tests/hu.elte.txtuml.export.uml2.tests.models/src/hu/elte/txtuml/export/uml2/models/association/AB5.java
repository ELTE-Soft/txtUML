package hu.elte.txtuml.export.uml2.models.association;

import hu.elte.txtuml.api.model.Association;

public class AB5 extends Association {
	class AEnd extends HiddenOne<A> {
	}

	class BEnd extends HiddenSome<B> {
	}
}