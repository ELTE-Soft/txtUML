package hu.elte.txtuml.api.model.execution.testmodel.assoc;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.execution.testmodel.A;

public class A_A extends Association {
	public class a1 extends Many<A> {
	}

	public class a2 extends Many<A> {
	}
}