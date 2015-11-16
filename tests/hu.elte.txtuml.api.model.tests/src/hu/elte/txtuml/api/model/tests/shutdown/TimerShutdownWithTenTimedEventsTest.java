package hu.elte.txtuml.api.model.tests.shutdown;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelExecutor;
import hu.elte.txtuml.api.model.tests.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.model.tests.models.simple.Sig;
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
			Timer.start(a, new Sig(), i * 10);
		}

		Assert.assertEquals(false, actionPerformed.value);
		Timer.shutdown();
		ModelExecutor.awaitTermination();
		Assert.assertEquals(true, actionPerformed.value);

		executionAsserter.assertEvents(x -> {
			transition(x, a, a.new Initialize());
			x.processingSignal(a, new Sig());
			transition(x, a, a.new S1_S2());
			x.processingSignal(a, new Sig());
			transition(x, a, a.new S2_S1());
			x.processingSignal(a, new Sig());
			transition(x, a, a.new S1_S2());
			x.processingSignal(a, new Sig());
			transition(x, a, a.new S2_S1());
			x.processingSignal(a, new Sig());
			transition(x, a, a.new S1_S2());
			x.processingSignal(a, new Sig());
			transition(x, a, a.new S2_S1());
			x.processingSignal(a, new Sig());
			transition(x, a, a.new S1_S2());
			x.processingSignal(a, new Sig());
			transition(x, a, a.new S2_S1());
			x.processingSignal(a, new Sig());
			transition(x, a, a.new S1_S2());
			x.processingSignal(a, new Sig());
			transition(x, a, a.new S2_S1());
			x.executionTerminated();
		});
	}
}
