package hu.elte.txtuml.api.model.tests.error.other;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelInt;
import hu.elte.txtuml.api.model.ModelValue;
import hu.elte.txtuml.api.model.tests.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.model.tests.models.SimpleModel.A;
import hu.elte.txtuml.api.model.tests.util.SeparateClassloaderTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class ModelObjectCreationFailureTest extends SimpleModelTestsBase {

	@Test
	public void test() {

		Action.create(A.class, new ModelInt(100));

		stopModelExecution();

		executionAsserter.assertErrors( x -> x.modelObjectCreationFailed(A.class,
						new ModelValue[] { new ModelInt(100) }));
	}

}
