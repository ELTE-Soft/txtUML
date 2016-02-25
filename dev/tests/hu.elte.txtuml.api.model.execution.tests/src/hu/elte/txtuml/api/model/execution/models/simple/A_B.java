package hu.elte.txtuml.api.model.execution.models.simple;

import hu.elte.txtuml.api.model.Association;

public class A_B extends Association {
	public class a extends MaybeOne<A> {
	}

	public class b extends One<B> {
	}
}