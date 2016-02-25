package hu.elte.txtuml.api.model.execution.error.multiplicity;

import org.junit.Test;
import org.junit.runner.RunWith;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.model.execution.models.simple.A_B;
import hu.elte.txtuml.api.model.execution.models.simple.B;
import hu.elte.txtuml.api.model.execution.util.SeparateClassloaderTestRunner;

@RunWith(SeparateClassloaderTestRunner.class)
public class UpperBoundOffendedTest extends SimpleModelTestsBase {

	@Test
	public void test() {
		Action.link(A_B.a.class, a, A_B.b.class, b);
		B b2 = new B();
		Action.link(A_B.a.class, a, A_B.b.class, b2);

		stopModelExecution();

		executionAsserter.assertErrors(x -> x.upperBoundOfMultiplicityOffended(
				a, A_B.b.class));

	}
}
