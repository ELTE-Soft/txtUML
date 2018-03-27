package hu.elte.txtuml.api.model.execution.testmodel.assoc;

import hu.elte.txtuml.api.model.Any;
import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.execution.testmodel.A;
import hu.elte.txtuml.api.model.execution.testmodel.B;

public class A_B_2 extends Association {
	public class a extends End<Any<A>> {
	}

	public class b extends HiddenEnd<Any<B>> {
	}
}