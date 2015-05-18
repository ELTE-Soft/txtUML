package hu.elte.txtuml.api.tests.base;

import hu.elte.txtuml.api.Action;
import hu.elte.txtuml.api.tests.models.ChoiceModel.A;

import org.junit.Before;

public class ChoiceModelTestsBase extends TestsBase {

	protected A a;

	@Before
	public void initializeModel() {
		a = new A();
		Action.start(a);
	}
}

