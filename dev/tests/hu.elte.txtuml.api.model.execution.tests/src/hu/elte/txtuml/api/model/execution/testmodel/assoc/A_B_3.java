package hu.elte.txtuml.api.model.execution.testmodel.assoc;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.execution.testmodel.A;
import hu.elte.txtuml.api.model.execution.testmodel.B;

public class A_B_3 extends Association {
	public class a extends Many<A> {
	}

	public class b extends Many<B> {
	}
}