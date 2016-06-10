package hu.elte.txtuml.api.model.execution.unittests;

import org.junit.Test;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.testmodel.assoc.A_B;
import hu.elte.txtuml.api.model.execution.testmodel.signals.Sig0;

public class DeletionErrorTests extends UnitTestsBase {

	@Test
	public void testCannotBeDeleted() {
		executor.run(() -> {
			createAAndB();
			Action.link(A_B.a.class, a, A_B.b.class, b);
			Action.delete(a);
		});

		executionAsserter.assertErrors(x -> x.objectCannotBeDeleted(a));
	}

	@Test
	public void testLinkingDeleted() {
		executor.run(() -> {
			createAAndB();
			Action.delete(a);
			Action.link(A_B.a.class, a, A_B.b.class, b);
		});

		executionAsserter.assertErrors(x -> x.linkingDeletedObject(a));
	}

	@Test
	public void testSignalArrivingToDeleted() {
		executor.run(() -> {
			createAAndB();
			Action.delete(a);
			Action.send(new Sig0(), a);
		});

		executionAsserter.assertWarnings(x -> x.signalArrivedToDeletedObject(a, new Sig0()));
	}

	@Test
	public void testStartingDeleted() {
		executor.run(() -> {
			createAAndB();
			Action.delete(a);
			Action.start(a);
		});

		executionAsserter.assertErrors(x -> x.startingDeletedObject(a));
	}

	@Test
	public void testUnlinkingDeleted() {
		executor.run(() -> {
			createAAndB();
			Action.delete(a);
			Action.unlink(A_B.a.class, a, A_B.b.class, b);
		});

		executionAsserter.assertErrors(x -> x.unlinkingDeletedObject(a));
	}

}
