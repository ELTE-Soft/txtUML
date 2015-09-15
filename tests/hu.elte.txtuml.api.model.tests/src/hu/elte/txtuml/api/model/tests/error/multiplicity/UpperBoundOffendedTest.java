package hu.elte.txtuml.api.model.tests.error.multiplicity;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.tests.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.model.tests.models.SimpleModel.A_B;
import hu.elte.txtuml.api.model.tests.models.SimpleModel.B;
import hu.elte.txtuml.api.model.tests.util.SeparateClassloaderTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class UpperBoundOffendedTest extends SimpleModelTestsBase {
	
	@Test
	public void test() {
		Action.link(A_B.a.class, a, A_B.b.class, b);
		B b2 = new B();
		Action.link(A_B.a.class, a, A_B.b.class, b2);
		
		stopModelExecution();

		executionAsserter.assertErrors( x -> x.upperBoundOfMultiplicityOffended(a, A_B.b.class));

	}
}
