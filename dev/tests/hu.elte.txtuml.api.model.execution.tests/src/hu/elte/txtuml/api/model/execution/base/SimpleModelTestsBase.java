package hu.elte.txtuml.api.model.execution.base;

import org.junit.Before;

import hu.elte.txtuml.api.model.execution.models.simple.A;
import hu.elte.txtuml.api.model.execution.models.simple.B;

public class SimpleModelTestsBase extends TestsBase {

	protected A a;
	protected B b;

	@Before
	public void initializeModel() {
		a = new A();
		b = new B();
	}
}
