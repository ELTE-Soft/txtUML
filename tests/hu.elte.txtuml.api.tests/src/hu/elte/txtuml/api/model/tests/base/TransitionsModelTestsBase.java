package hu.elte.txtuml.api.model.tests.base;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.tests.models.TransitionsModel.A;

import org.junit.Before;

public class TransitionsModelTestsBase extends TestsBase {

	protected A a;

	@Before
	public void initializeModel() {
		a = new A();
		Action.start(a);
	}
}
