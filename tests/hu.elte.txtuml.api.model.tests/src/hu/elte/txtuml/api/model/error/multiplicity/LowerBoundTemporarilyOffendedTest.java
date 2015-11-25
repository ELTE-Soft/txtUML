package hu.elte.txtuml.api.model.error.multiplicity;

import org.junit.Test;
import org.junit.runner.RunWith;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.model.tests.models.simple.A_B;
import hu.elte.txtuml.api.model.util.SeparateClassloaderTestRunner;

@RunWith(SeparateClassloaderTestRunner.class)
public class LowerBoundTemporarilyOffendedTest extends SimpleModelTestsBase {

	@Test
	public void test() {
		Action.link(A_B.a.class, a, A_B.b.class, b);
		Action.unlink(A_B.a.class, a, A_B.b.class, b);
		Action.start(a);
		Action.link(A_B.a.class, a, A_B.b.class, b);

		stopModelExecution();

		executionAsserter.assertErrors(x -> {
		});
	}
}
