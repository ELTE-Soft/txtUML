package hu.elte.txtuml.api.model.execution.base;

import hu.elte.txtuml.api.model.execution.models.simple.A;
import hu.elte.txtuml.api.model.execution.models.simple.B;

public class SimpleModelTestsBase extends TestsBase {

	protected A a;
	protected B b;

	protected void createAAndB() {
		a = new A();
		b = new B();
	}
}
