package hu.elte.txtuml.examples.performance_test.model;

import hu.elte.txtuml.api.model.Any;
import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.ZeroToOne;

public class AB extends Association {
	public class a extends End<ZeroToOne<A>> {
	}

	public class b extends End<Any<B>> {
	}
}