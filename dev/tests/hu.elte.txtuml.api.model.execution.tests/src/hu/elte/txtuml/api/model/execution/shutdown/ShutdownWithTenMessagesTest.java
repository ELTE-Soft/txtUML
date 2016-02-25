package hu.elte.txtuml.api.model.execution.shutdown;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelExecutor;
import hu.elte.txtuml.api.model.execution.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.model.execution.models.simple.Sig;
import hu.elte.txtuml.api.model.execution.util.MutableBoolean;
import hu.elte.txtuml.api.model.execution.util.SeparateClassloaderTestRunner;

@RunWith(SeparateClassloaderTestRunner.class)
public class ShutdownWithTenMessagesTest extends SimpleModelTestsBase {

	@Test
	public void test() {
		MutableBoolean actionPerformed = new MutableBoolean();
		ModelExecutor.addToShutdownQueue(() -> actionPerformed.value = true);

		Action.start(a);
		for (int i = 0; i < 10; ++i) {
			Action.send(new Sig(), a);
		}

		Assert.assertEquals(false, actionPerformed.value);
		stopModelExecution();
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
