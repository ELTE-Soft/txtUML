package hu.elte.txtuml.examples.performance_test;

import java.io.IOException;
import java.io.OutputStream;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelExecutor;
import hu.elte.txtuml.examples.performance_test.model.Test;

public class PerformanceTester {

	public static class NullOutputStream extends OutputStream {
		@Override
		public void write(int b) throws IOException {
		}
	}

	public static void main(String[] args) {
		ModelExecutor.Settings.setExecutorLog(true);

		long start = System.currentTimeMillis();

		ModelExecutor.Settings.setDynamicChecks(false);

		Test m = Action.create(Test.class);

		// PerformanceTestModel.Test m = Action
		// .create(PerformanceTestModel.Test.class);

		m.test();

		ModelExecutor.shutdown();

		System.out.println(System.currentTimeMillis() - start);

	}

}