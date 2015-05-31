package hu.elte.txtuml.api.tests.shutdown;

import hu.elte.txtuml.api.ModelExecutor;
import hu.elte.txtuml.api.backend.messages.LogMessages;
import hu.elte.txtuml.api.tests.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.tests.util.MutableBoolean;
import hu.elte.txtuml.api.tests.util.SeparateClassloaderTestRunner;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class ShutdownWithZeroMessageTest extends SimpleModelTestsBase {

	@Test
	public void test() {
		MutableBoolean actionPerformed = new MutableBoolean();
		ModelExecutor.addToShutdownQueue(() -> actionPerformed.value = true);

		Assert.assertEquals(false, actionPerformed.value);
		stopModelExecution(() -> {
			ModelExecutor.shutdown();
			Assert.assertEquals(false, actionPerformed.value);			
		});
		Assert.assertEquals(true, actionPerformed.value);

		Assert.assertArrayEquals(
				new String[] { LogMessages.getModelExecutionShutdownMessage() },
				executorStream.getOutputAsArray());
	}

}
