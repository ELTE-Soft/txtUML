package hu.elte.txtuml.api.tests.error.other;

import hu.elte.txtuml.api.Action;
import hu.elte.txtuml.api.ModelInt;
import hu.elte.txtuml.api.ModelValue;
import hu.elte.txtuml.api.backend.messages.ErrorMessages;
import hu.elte.txtuml.api.tests.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.tests.models.SimpleModel.A;
import hu.elte.txtuml.api.tests.util.SeparateClassloaderTestRunner;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class ModelObjectCreationFailureTest extends SimpleModelTestsBase {

	@Test
	public void test() {

		Action.create(A.class, new ModelInt(100));

		stopModelExecution();

		Assert.assertArrayEquals(new String[] { ErrorMessages
				.getModelObjectCreationFailedMessage(A.class,
						new ModelValue[] { new ModelInt(100) }) },
				executorErrorStream.getOutputAsArray());

	}

}
