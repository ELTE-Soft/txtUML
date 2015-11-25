package hu.elte.txtuml.api.model.base;

import org.junit.Before;

import hu.elte.txtuml.api.model.models.choice.A;

public class ChoiceModelTestsBase extends TestsBase {

	protected A a;

	@Before
	public void initializeModel() {
		a = new A();
	}
}
