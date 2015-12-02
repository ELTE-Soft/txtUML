package hu.elte.txtuml.api.model.error.other;

import org.junit.Test;
import org.junit.runner.RunWith;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.model.models.simple.A_B;
import hu.elte.txtuml.api.model.util.SeparateClassloaderTestRunner;

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
