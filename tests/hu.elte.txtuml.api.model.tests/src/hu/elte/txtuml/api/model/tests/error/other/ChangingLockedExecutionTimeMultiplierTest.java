package hu.elte.txtuml.api.model.tests.error.other;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelExecutor;
import hu.elte.txtuml.api.model.tests.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.model.tests.models.simple.A_B;
import hu.elte.txtuml.api.model.tests.models.simple.Sig;
import hu.elte.txtuml.api.model.tests.util.SeparateClassloaderTestRunner;
import hu.elte.txtuml.api.stdlib.Timer;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class ChangingLockedExecutionTimeMultiplierTest extends
		SimpleModelTestsBase {

	@Test
	public void test() {

		Action.link(A_B.a.class, a, A_B.b.class, b);

		Action.start(a);

		Timer.start(a, new Sig(), 10);
		ModelExecutor.Settings.setExecutionTimeMultiplier(1.0f);

		Timer.shutdown();
		ModelExecutor.awaitTermination();

		executionAsserter.assertErrors(x -> x
				.changingLockedExecutionTimeMultiplier());

	}

}
