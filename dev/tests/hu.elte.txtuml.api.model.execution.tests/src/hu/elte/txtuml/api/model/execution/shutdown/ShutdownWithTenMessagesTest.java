package hu.elte.txtuml.api.model.execution.shutdown;

import org.junit.Assert;
import org.junit.Test;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.model.execution.models.simple.Sig;
import hu.elte.txtuml.api.model.execution.util.MutableBoolean;

public class ShutdownWithTenMessagesTest extends SimpleModelTestsBase {

	@Test
	public void test() {
		MutableBoolean actionPerformed = new MutableBoolean();

		executor.addTerminationListener(() -> actionPerformed.value = true);

		executor.launch(() -> {
			createAAndB();

			Action.start(a);
			for (int i = 0; i < 10; ++i) {
				Action.send(new Sig(), a);
			}

		});

		Assert.assertFalse(actionPerformed.value);

		executor.shutdown().awaitTermination();

		Assert.assertTrue(actionPerformed.value);

		executionAsserter.assertEvents(x -> {
			x.executionStarted();
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
