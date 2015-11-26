package hu.elte.txtuml.api.model.error.multiplicity;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.model.models.SimpleModel.A_B;
import hu.elte.txtuml.api.model.util.SeparateClassloaderTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class LowerBoundOffendedAfterUnlinkTest extends SimpleModelTestsBase {

	@Test
	public void test() {
		Action.link(A_B.a.class, a, A_B.b.class, b);
		Action.unlink(A_B.a.class, a, A_B.b.class, b);
		Action.start(a);

		stopModelExecution();

		executionAsserter.assertErrors(x -> x.lowerBoundOfMultiplicityOffended(
				a, A_B.b.class));

	}

}
