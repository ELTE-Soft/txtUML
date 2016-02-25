package hu.elte.txtuml.api.model.execution.error.other;

import org.junit.Test;
import org.junit.runner.RunWith;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.model.execution.models.simple.A;
import hu.elte.txtuml.api.model.execution.util.SeparateClassloaderTestRunner;

@RunWith(SeparateClassloaderTestRunner.class)
public class ModelObjectCreationFailureTest extends SimpleModelTestsBase {

	@Test
	public void test() {

		Action.create(A.class, 100);

		stopModelExecution();

		executionAsserter.assertErrors(x -> x.modelObjectCreationFailed(
				A.class, new Object[] { 100 }));
	}

}
