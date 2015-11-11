package hu.elte.txtuml.api.model.error.deletion;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.model.util.SeparateClassloaderTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

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
