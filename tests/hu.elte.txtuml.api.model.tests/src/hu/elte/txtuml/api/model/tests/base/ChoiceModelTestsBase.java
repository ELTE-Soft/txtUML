package hu.elte.txtuml.api.model.tests.base;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.tests.models.choice.A;

import org.junit.Before;

public class ChoiceModelTestsBase extends TestsBase {

	protected A a;

	@Before
	public void initializeModel() {
		a = new A();
		Action.start(a);
	}
}
