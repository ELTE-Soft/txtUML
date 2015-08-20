package hu.elte.txtuml.api.tests.error.deletion;

import hu.elte.txtuml.api.Action;
import hu.elte.txtuml.api.backend.messages.ErrorMessages;
import hu.elte.txtuml.api.tests.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.tests.util.SeparateClassloaderTestRunner;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class StartingDeletedTest extends SimpleModelTestsBase {

	@Test
	public void test() {
		
		Action.delete(a);
		Action.start(a);
		
		stopModelExecution();

		Assert.assertArrayEquals(
				new String[] { ErrorMessages.getStartingDeletedObjectMessage(a) },
				executorErrorStream.getOutputAsArray());

	}

}
