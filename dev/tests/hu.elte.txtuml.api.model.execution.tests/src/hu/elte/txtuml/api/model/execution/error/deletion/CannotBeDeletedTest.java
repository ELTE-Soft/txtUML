package hu.elte.txtuml.api.model.execution.error.deletion;

import org.junit.Test;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.model.execution.models.simple.A_B;

public class CannotBeDeletedTest extends SimpleModelTestsBase {

	@Test
	public void test() {
		executor.run(() -> {
			createAAndB();
			Action.link(A_B.a.class, a, A_B.b.class, b);
			Action.delete(a);
		});

		executionAsserter.assertErrors(x -> x.objectCannotBeDeleted(a));

	}
}
