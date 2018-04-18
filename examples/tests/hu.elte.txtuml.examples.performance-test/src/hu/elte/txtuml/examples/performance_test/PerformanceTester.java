package hu.elte.txtuml.examples.performance_test;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.CheckLevel;
import hu.elte.txtuml.api.model.execution.LogLevel;
import hu.elte.txtuml.api.model.execution.ModelExecutor;
import hu.elte.txtuml.examples.performance_test.model.Test;

public class PerformanceTester {

	public static void main(String[] args) {
		ModelExecutor executor = ModelExecutor.create().setLogLevel(LogLevel.TRACE).setCheckLevel(CheckLevel.MANDATORY);

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