package hu.elte.txtuml.api.model.tests.shutdown;

import hu.elte.txtuml.api.model.ModelExecutor;
import hu.elte.txtuml.api.model.tests.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.model.tests.util.SeparateClassloaderTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class ShutdownNowTest extends SimpleModelTestsBase {

	@Test
	public void test() {
		ModelExecutor.shutdownNow();

		executionAsserter.assertEvents(x -> {
			x.executionTerminated();
		});

	}

}
