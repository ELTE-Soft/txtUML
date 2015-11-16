package hu.elte.txtuml.api.model.tests.base;

import hu.elte.txtuml.api.model.tests.models.simple.A;
import hu.elte.txtuml.api.model.tests.models.simple.B;

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
