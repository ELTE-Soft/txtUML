package hu.elte.txtuml.api.model.execution.unittests;

import org.junit.Assert;
import org.junit.Test;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.testmodel.signals.Sig0;
import hu.elte.txtuml.api.model.execution.util.MutableBoolean;

public class ShutdownTests extends UnitTestsBase {

	@Test
	public void testShutdownWithTenMessages() {
		MutableBoolean actionPerformed = new MutableBoolean();

		executor.addTerminationListener(() -> actionPerformed.value = true);

		executor.start(() -> {
			createAAndB();

			Action.start(a);
			for (int i = 0; i < 10; ++i) {
				Action.send(new Sig0(), a);
			}

		});

		Assert.assertFalse(actionPerformed.value);

		executor.shutdown().awaitTermination();

		Assert.assertTrue(actionPerformed.value);

		executionAsserter.assertEvents(x -> {
			x.executionStarted();
			transition(x, a, a.new Initialize());
			x.processingSignal(a, new Sig0());
			transition(x, a, a.new S1_S2());
			x.processingSignal(a, new Sig0());
			transition(x, a, a.new S2_S1());
			x.processingSignal(a, new Sig0());
			transition(x, a, a.new S1_S2());
			x.processingSignal(a, new Sig0());
			transition(x, a, a.new S2_S1());
			x.processingSignal(a, new Sig0());
			transition(x, a, a.new S1_S2());
			x.processingSignal(a, new Sig0());
			transition(x, a, a.new S2_S1());
			x.processingSignal(a, new Sig0());
			transition(x, a, a.new S1_S2());
			x.processingSignal(a, new Sig0());
			transition(x, a, a.new S2_S1());
			x.processingSignal(a, new Sig0());
			transition(x, a, a.new S1_S2());
			x.processingSignal(a, new Sig0());
			transition(x, a, a.new S2_S1());
			x.executionTerminated();
		});
	}

}
