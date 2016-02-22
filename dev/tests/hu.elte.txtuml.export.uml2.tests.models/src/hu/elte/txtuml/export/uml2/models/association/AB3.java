package hu.elte.txtuml.export.uml2.models.association;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.Max;
import hu.elte.txtuml.api.model.Min;

public class AB3 extends Association {
	@Min(3)
	@Max(4)
	class AEnd extends Multiple<A> {
	}

	@Min(0)
	@Max(100)
	class BEnd extends Multiple<B> {
	}
}