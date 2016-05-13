package hu.elte.txtuml.api.model.execution.testmodel.assoc;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.execution.testmodel.A;
import hu.elte.txtuml.api.model.execution.testmodel.B;

public class A_B extends Association {
	public class a extends MaybeOne<A> {
	}

	public class b extends One<B> {
	}
}