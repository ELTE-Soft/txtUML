package hu.elte.txtuml.api.tests.base;

import hu.elte.txtuml.api.tests.models.SimpleModel.A;
import hu.elte.txtuml.api.tests.models.SimpleModel.B;

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
