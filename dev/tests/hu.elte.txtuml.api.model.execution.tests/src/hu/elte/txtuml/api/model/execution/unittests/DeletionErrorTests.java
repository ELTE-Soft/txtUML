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

		assertErrors(x -> x.objectCannotBeDeleted(a));
		assertNoWarnings();
	}

	@Test
	public void testLinkingDeleted() {
		executor.run(() -> {
			createAAndB();
			Action.delete(a);
			Action.link(A_B.a.class, a, A_B.b.class, b);
		});

		assertErrors(x -> x.linkingDeletedObject(a));
		assertNoWarnings();
	}

	@Test
	public void testSignalArrivingToDeleted() {
		executor.run(() -> {
			createAAndB();
			Action.delete(a);
			Action.send(new Sig0(), a);
		});

		assertNoErrors();
		assertWarnings(x -> x.signalArrivedToDeletedObject(a, new Sig0()));
	}

	@Test
	public void testStartingDeleted() {
		executor.run(() -> {
			createAAndB();
			Action.delete(a);
			Action.start(a);
		});

		assertErrors(x -> x.startingDeletedObject(a));
		assertNoWarnings();
	}

	@Test
	public void testUnlinkingDeleted() {
		executor.run(() -> {
			createAAndB();
			Action.delete(a);
			Action.unlink(A_B.a.class, a, A_B.b.class, b);
		});

		assertErrors(x -> x.unlinkingDeletedObject(a));
		assertNoWarnings();
	}

}
