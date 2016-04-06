package hu.elte.txtuml.api.model.execution.error.deletion;

import org.junit.Test;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.base.SimpleModelTestsBase;

public class StartingDeletedTest extends SimpleModelTestsBase {

	@Test
	public void test() {
		executor.run(() -> {
			createAAndB();
			Action.delete(a);
			Action.start(a);
		});

		executionAsserter.assertErrors(x -> x.startingDeletedObject(a));

	}
}
