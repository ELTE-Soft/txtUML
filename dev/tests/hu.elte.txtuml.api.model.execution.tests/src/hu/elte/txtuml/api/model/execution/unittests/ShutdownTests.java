package hu.elte.txtuml.api.model.execution.unittests;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.testmodel.signals.Sig0;

public class ShutdownTests extends UnitTestsBase {

	@Test
	public void testShutdownWithTenMessages() {
		boolean[] actionPerformed = new boolean[] { false };

		executor.addTerminationListener(() -> actionPerformed[0] = true);

		executor.start(() -> {
			createAAndB();

			Action.start(a);
			for (int i = 0; i < 10; ++i) {
				Action.send(new Sig0(), a);
			}

		});

		Assert.assertFalse(actionPerformed[0]);

		executor.shutdown().awaitTermination();

		Assert.assertTrue(actionPerformed[0]);

		assertEvents(x -> {
			x.executionStarted();
			transition(x, a, a.new Initialize());

			for (int i = 0; i < 5; ++i) {
				x.processingSignal(a, new Sig0(), Optional.empty());
				transition(x, a, a.new S1_S2());
				x.processingSignal(a, new Sig0(), Optional.empty());
				transition(x, a, a.new S2_S1());
			}

			x.executionTerminated();
		});
		assertNoErrors();
		assertNoWarnings();
	}

}
