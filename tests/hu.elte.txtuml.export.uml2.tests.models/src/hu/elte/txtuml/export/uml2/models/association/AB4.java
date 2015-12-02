package hu.elte.txtuml.export.uml2.models.association;

import hu.elte.txtuml.api.model.Association;

public class AB4 extends Association {
	class AEnd extends HiddenMaybeOne<A> {
	}

	class BEnd extends HiddenMany<B> {
	}
}