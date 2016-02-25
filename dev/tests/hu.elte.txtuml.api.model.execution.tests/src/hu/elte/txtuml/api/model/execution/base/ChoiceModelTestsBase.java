package hu.elte.txtuml.api.model.execution.base;

import org.junit.Before;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.models.choice.A;

public class ChoiceModelTestsBase extends TestsBase {

	protected A a;

	@Before
	public void initializeModel() {
		a = new A();
		Action.start(a);
	}
}
