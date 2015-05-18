package hu.elte.txtuml.api.tests.error.deletion;

import hu.elte.txtuml.api.Action;
import hu.elte.txtuml.api.backend.messages.ErrorMessages;
import hu.elte.txtuml.api.tests.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.tests.models.SimpleModel.A_B;
import hu.elte.txtuml.api.tests.util.SeparateClassloaderTestRunner;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class CannotBeDeletedTest extends SimpleModelTestsBase {

	@Test
	public void test() {
		
		Action.link(A_B.a.class, a, A_B.b.class, b);
		Action.delete(a);
		
		stopModelExecution();

		Assert.assertArrayEquals(
				new String[] { ErrorMessages.getObjectCannotBeDeletedMessage(a) },
				executorErrorStream.getOutputAsArray());

	}

}
