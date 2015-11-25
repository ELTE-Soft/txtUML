package hu.elte.txtuml.api.model.base;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.models.HierarchicalModel.A;

import org.junit.Before;

public class HierarchicalModelTestsBase extends TestsBase {

	protected A a;

	@Before
	public void initializeModel() {
		a = new A();
		Action.start(a);
	}
}
