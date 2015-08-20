package hu.elte.txtuml.api.tests.shutdown;

import hu.elte.txtuml.api.ModelExecutor;
import hu.elte.txtuml.api.backend.messages.LogMessages;
import hu.elte.txtuml.api.tests.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.tests.util.SeparateClassloaderTestRunner;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class ShutdownNowTest extends SimpleModelTestsBase {

	@Test
	public void test() {
		ModelExecutor.shutdownNow();

		Assert.assertArrayEquals(
				new String[] { LogMessages.getModelExecutionShutdownMessage() },
				executorStream.getOutputAsArray());

	}

}
