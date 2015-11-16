package hu.elte.txtuml.api.model.tests.base;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.tests.models.hierarchical.A;

import org.junit.Before;

public class HierarchicalModelTestsBase extends TestsBase {

	protected A a;

	@Before
	public void initializeModel() {
		a = new A();
		Action.start(a);
	}
}
