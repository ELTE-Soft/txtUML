package hu.elte.txtuml.layout.export.model1.associations;

import hu.elte.txtuml.api.model.Any;
import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.One;
import hu.elte.txtuml.layout.export.model1.A;
import hu.elte.txtuml.layout.export.model1.B;

public class S extends Association {
	public class e1 extends End<Any<A>> {
	}

	public class e2 extends End<One<B>> {
	}
}
