package hu.elte.txtuml.api.model.execution.testmodel.assoc;

import hu.elte.txtuml.api.model.Any;
import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.execution.testmodel.A;

public class A_A extends Association {
	public class a1 extends End<Any<A>> {
	}

	public class a2 extends End<Any<A>> {
	}
}