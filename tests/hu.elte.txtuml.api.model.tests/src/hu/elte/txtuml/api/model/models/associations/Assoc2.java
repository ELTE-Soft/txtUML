package hu.elte.txtuml.api.model.models.associations;

import hu.elte.txtuml.api.model.Association;

public class Assoc2 extends Association {
	public class a extends Many<A> {
	}

	public class b extends Many<B> {
	}
}