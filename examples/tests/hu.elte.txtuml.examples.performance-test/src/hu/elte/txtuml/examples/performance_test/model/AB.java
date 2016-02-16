package hu.elte.txtuml.examples.performance_test.model;

import hu.elte.txtuml.api.model.Association;

public class AB extends Association {
	public class a extends Association.MaybeOne<A> {
	}

	public class b extends Association.Many<B> {
	}
}