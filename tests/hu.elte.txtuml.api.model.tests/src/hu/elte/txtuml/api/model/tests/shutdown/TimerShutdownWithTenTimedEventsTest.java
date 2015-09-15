package hu.elte.txtuml.api.model.tests.shutdown;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelExecutor;
import hu.elte.txtuml.api.model.ModelInt;
import hu.elte.txtuml.api.model.tests.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.model.tests.models.SimpleModel.Sig;
import hu.elte.txtuml.api.model.tests.util.MutableBoolean;
import hu.elte.txtuml.api.model.tests.util.SeparateClassloaderTestRunner;
import hu.elte.txtuml.api.stdlib.Timer;

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

		/*
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
		*/
	}

}
