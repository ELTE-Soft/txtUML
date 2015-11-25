package hu.elte.txtuml.api.model.error.other;

import org.junit.Test;
import org.junit.runner.RunWith;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.model.models.simple.A;
import hu.elte.txtuml.api.model.util.SeparateClassloaderTestRunner;

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
