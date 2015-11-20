package hu.elte.txtuml.api.model.base;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.models.ChoiceModel.A;

import org.junit.Before;

public class ChoiceModelTestsBase extends TestsBase {

	protected A a;

	@Before
	public void initializeModel() {
		a = new A();
		Action.start(a);
	}
}

