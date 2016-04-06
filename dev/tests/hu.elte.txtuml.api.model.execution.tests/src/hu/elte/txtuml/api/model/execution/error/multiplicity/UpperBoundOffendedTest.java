package hu.elte.txtuml.api.model.execution.error.multiplicity;

import org.junit.Test;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.model.execution.models.simple.A_B;
import hu.elte.txtuml.api.model.execution.models.simple.B;

public class UpperBoundOffendedTest extends SimpleModelTestsBase {

	@Test
	public void test() {
		executor.run(() -> {
			createAAndB();
			Action.link(A_B.a.class, a, A_B.b.class, b);
			B b2 = new B();
			Action.link(A_B.a.class, a, A_B.b.class, b2);
		});

		executionAsserter.assertErrors(x -> x.upperBoundOfMultiplicityOffended(a, A_B.b.class));

	}
}
