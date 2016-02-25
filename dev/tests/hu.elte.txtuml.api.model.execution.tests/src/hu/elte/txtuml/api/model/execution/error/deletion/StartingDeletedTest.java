package hu.elte.txtuml.api.model.execution.error.deletion;

import org.junit.Test;
import org.junit.runner.RunWith;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.model.execution.util.SeparateClassloaderTestRunner;

@RunWith(SeparateClassloaderTestRunner.class)
public class StartingDeletedTest extends SimpleModelTestsBase {

	@Test
	public void test() {

		Action.delete(a);
		Action.start(a);

		stopModelExecution();

		executionAsserter.assertErrors(x -> x.startingDeletedObject(a));

	}
}
