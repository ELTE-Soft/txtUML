package hu.elte.txtuml.api.model.base;

import hu.elte.txtuml.api.model.models.SimpleModel.A;
import hu.elte.txtuml.api.model.models.SimpleModel.B;

import org.junit.Before;

public class SimpleModelTestsBase extends TestsBase {

	protected A a;
	protected B b;

	@Before
	public void initializeModel() {
		a = new A();
		b = new B();
	}
}
