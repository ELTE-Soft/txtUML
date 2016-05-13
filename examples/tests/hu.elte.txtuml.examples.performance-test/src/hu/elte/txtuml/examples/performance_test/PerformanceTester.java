package hu.elte.txtuml.examples.performance_test;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.ModelExecutor;
import hu.elte.txtuml.examples.performance_test.model.Test;

public class PerformanceTester {

	public static void main(String[] args) {
		ModelExecutor executor = ModelExecutor.create().setTraceLogging(true).setDynamicChecks(false);

		long start = System.currentTimeMillis();

		executor.run(() -> {
			Test m = Action.create(Test.class);
			// PerformanceTestModel.Test m = Action
			// .create(PerformanceTestModel.Test.class);

			m.test();
		});

		System.out.println(System.currentTimeMillis() - start);

	}

}