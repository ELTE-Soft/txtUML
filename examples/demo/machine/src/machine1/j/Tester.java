package machine1.j;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.ModelExecutor;
import machine1.j.model.ModelTester;

public class Tester {

	static void init() {
		ModelTester tester = Action.create(ModelTester.class);
		tester.test();
	}

	public static void main(String[] args) {
		ModelExecutor.create().setTraceLogging(false).run(Tester::init);
	}

}
