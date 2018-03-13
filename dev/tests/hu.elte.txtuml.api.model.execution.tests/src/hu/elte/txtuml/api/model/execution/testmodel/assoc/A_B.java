package hu.elte.txtuml.api.model.execution.testmodel.assoc;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.One;
import hu.elte.txtuml.api.model.ZeroToOne;
import hu.elte.txtuml.api.model.execution.testmodel.A;
import hu.elte.txtuml.api.model.execution.testmodel.B;

public class A_B extends Association {
	public class a extends End<ZeroToOne<A>> {
	}

	public class b extends End<One<B>> {
	}
}