package hu.elte.txtuml.api.model.base;

import org.junit.Before;

import hu.elte.txtuml.api.model.tests.models.simple.A;
import hu.elte.txtuml.api.model.tests.models.simple.B;

public class SimpleModelTestsBase extends TestsBase {

	protected A a;
	protected B b;

	@Before
	public void initializeModel() {
		a = new A();
		b = new B();
	}
}
