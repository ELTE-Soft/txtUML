package hu.elte.txtuml.api.model.execution.error.multiplicity;

import org.junit.Test;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.base.SimpleModelTestsBase;

public class LowerBoundInitiallyOffendedTest extends SimpleModelTestsBase {

	@Test
	public void test() {
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

}
