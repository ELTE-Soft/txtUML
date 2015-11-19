package hu.elte.txtuml.api.model.shutdown;

import hu.elte.txtuml.api.model.ModelExecutor;
import hu.elte.txtuml.api.model.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.model.util.SeparateClassloaderTestRunner;

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
