package hu.elte.txtuml.api.model.execution.error.deletion;

import org.junit.Test;
import org.junit.runner.RunWith;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.model.execution.models.simple.A_B;
import hu.elte.txtuml.api.model.execution.util.SeparateClassloaderTestRunner;

@RunWith(SeparateClassloaderTestRunner.class)
public class CannotBeDeletedTest extends SimpleModelTestsBase {

	@Test
	public void test() {

		Action.link(A_B.a.class, a, A_B.b.class, b);
		Action.delete(a);

		stopModelExecution();

		executionAsserter.assertErrors(x -> x.objectCannotBeDeleted(a));

	}
}
