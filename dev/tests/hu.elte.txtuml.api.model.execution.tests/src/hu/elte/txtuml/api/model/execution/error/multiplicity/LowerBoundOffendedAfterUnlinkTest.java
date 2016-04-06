package hu.elte.txtuml.api.model.execution.error.multiplicity;

import org.junit.Test;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.model.execution.models.simple.A_B;
import hu.elte.txtuml.api.model.execution.models.simple.Sig;

public class LowerBoundOffendedAfterUnlinkTest extends SimpleModelTestsBase {

	@Test
	public void test() {
		executor.run(() -> {
			createAAndB();
			Action.link(A_B.a.class, a, A_B.b.class, b);
			Action.unlink(A_B.a.class, a, A_B.b.class, b);
			Action.start(a);
			Action.send(new Sig(), a);
		});

		executionAsserter.assertErrors(x -> x.lowerBoundOfMultiplicityOffended(a, A_B.b.class));

	}

}
