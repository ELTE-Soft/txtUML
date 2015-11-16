package hu.elte.txtuml.api.model.tests.error.other;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.tests.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.model.tests.models.simple.A_B;
import hu.elte.txtuml.api.model.tests.util.SeparateClassloaderTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class UnlinkingNonExistingAssociationTest extends SimpleModelTestsBase {

	@Test
	public void test() {

		Action.unlink(A_B.a.class, a, A_B.b.class, b);

		stopModelExecution();

		executionAsserter.assertWarnings(x -> x
				.unlinkingNonExistingAssociation(a, b));
	}

}
