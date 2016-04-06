package hu.elte.txtuml.api.model.execution.error.other;

import org.junit.Test;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.model.execution.models.simple.A_B;

public class UnlinkingNonExistingAssociationTest extends SimpleModelTestsBase {

	@Test
	public void test() {

		executor.run(() -> {
			createAAndB();
			Action.unlink(A_B.a.class, a, A_B.b.class, b);
		});

		executionAsserter.assertWarnings(x -> x.unlinkingNonExistingAssociation(A_B.a.class, a, A_B.b.class, b));
	}

}
