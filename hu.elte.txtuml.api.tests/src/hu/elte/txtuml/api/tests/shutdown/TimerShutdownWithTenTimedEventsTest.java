package hu.elte.txtuml.api.tests.shutdown;

import hu.elte.txtuml.api.Action;
import hu.elte.txtuml.api.ModelExecutor;
import hu.elte.txtuml.api.ModelInt;
import hu.elte.txtuml.api.backend.messages.LogMessages;
import hu.elte.txtuml.api.tests.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.tests.models.SimpleModel.Sig;
import hu.elte.txtuml.api.tests.util.MutableBoolean;
import hu.elte.txtuml.api.tests.util.SeparateClassloaderTestRunner;
import hu.elte.txtuml.stdlib.Timer;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class TimerShutdownWithTenTimedEventsTest extends SimpleModelTestsBase {

	@Test
	public void test() {
		MutableBoolean actionPerformed = new MutableBoolean();
		ModelExecutor.addToShutdownQueue(() -> actionPerformed.value = true);

		Action.start(a);
		for (int i = 0; i < 10; ++i) {
			Timer.start(a, new Sig(), new ModelInt(i * 10));
		}

		Assert.assertEquals(false, actionPerformed.value);
		stopModelExecution(() -> {
			Timer.shutdown();
			Assert.assertEquals(false, actionPerformed.value);			
		});
		Assert.assertEquals(true, actionPerformed.value);

		Assert.assertArrayEquals(
				new String[] {
						LogMessages.getUsingTransitionMessage(a,
								a.new Initialize()),
						LogMessages.getProcessingSignalMessage(a, new Sig()),
						LogMessages.getUsingTransitionMessage(a, a.new S1_S2()),
						LogMessages.getProcessingSignalMessage(a, new Sig()),
						LogMessages.getUsingTransitionMessage(a, a.new S2_S1()),
						LogMessages.getProcessingSignalMessage(a, new Sig()),
						LogMessages.getUsingTransitionMessage(a, a.new S1_S2()),
						LogMessages.getProcessingSignalMessage(a, new Sig()),
						LogMessages.getUsingTransitionMessage(a, a.new S2_S1()),
						LogMessages.getProcessingSignalMessage(a, new Sig()),
						LogMessages.getUsingTransitionMessage(a, a.new S1_S2()),
						LogMessages.getProcessingSignalMessage(a, new Sig()),
						LogMessages.getUsingTransitionMessage(a, a.new S2_S1()),
						LogMessages.getProcessingSignalMessage(a, new Sig()),
						LogMessages.getUsingTransitionMessage(a, a.new S1_S2()),
						LogMessages.getProcessingSignalMessage(a, new Sig()),
						LogMessages.getUsingTransitionMessage(a, a.new S2_S1()),
						LogMessages.getProcessingSignalMessage(a, new Sig()),
						LogMessages.getUsingTransitionMessage(a, a.new S1_S2()),
						LogMessages.getProcessingSignalMessage(a, new Sig()),
						LogMessages.getUsingTransitionMessage(a, a.new S2_S1()),
						LogMessages.getModelExecutionShutdownMessage() },
				executorStream.getOutputAsArray());
	}

}
