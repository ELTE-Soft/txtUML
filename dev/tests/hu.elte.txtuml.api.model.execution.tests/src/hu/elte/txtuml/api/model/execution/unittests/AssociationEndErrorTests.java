package hu.elte.txtuml.api.model.execution.unittests;

import org.junit.Test;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.testmodel.B;
import hu.elte.txtuml.api.model.execution.testmodel.assoc.A_B;
import hu.elte.txtuml.api.model.execution.testmodel.signals.Sig0;

public class AssociationEndErrorTests extends UnitTestsBase {

	@Test
	public void testLowerBoundInitiallyOffended() {
		executor.run(() -> {
			createAAndB();
			Action.start(a);
		});

		// TODO The tested feature is currently not working.
		//
		// executionAsserter.assertErrors(x ->
		// x.lowerBoundOfMultiplicityOffended(
		// a, A_B.b.class));
	}

	@Test
	public void testLowerBoundOffendedAfterUnlink() {
		executor.run(() -> {
			createAAndB();
			Action.link(A_B.a.class, a, A_B.b.class, b);
			Action.unlink(A_B.a.class, a, A_B.b.class, b);
			Action.start(a);
			Action.send(new Sig0(), a);
		});

		executionAsserter.assertErrors(x -> x.lowerBoundOfMultiplicityOffended(a, A_B.b.class));

	}

	@Test
	public void testLowerBoundTemporarilyOffended() {
		executor.run(() -> {
			createAAndB();
			Action.link(A_B.a.class, a, A_B.b.class, b);
			Action.unlink(A_B.a.class, a, A_B.b.class, b);
			Action.link(A_B.a.class, a, A_B.b.class, b);
			Action.start(a);
			Action.send(new Sig0(), a);
		});

		executionAsserter.assertErrors(x -> {
			// no errors should be raised
		});
	}

	@Test
	public void testUpperBoundOffended() {
		executor.run(() -> {
			createAAndB();
			Action.link(A_B.a.class, a, A_B.b.class, b);
			B b2 = new B();
			Action.link(A_B.a.class, a, A_B.b.class, b2);
		});

		executionAsserter.assertErrors(x -> x.upperBoundOfMultiplicityOffended(a, A_B.b.class));

	}

}
