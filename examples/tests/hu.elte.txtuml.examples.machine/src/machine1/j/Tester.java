package machine1.j;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.Execution;
import hu.elte.txtuml.api.model.execution.LogLevel;
import machine1.j.model.ModelTester;

public class Tester implements Execution {

	@Override
	public void configure(Settings s) {
		s.logLevel = LogLevel.TRACE;
	}

	@Override
	public void initialization() {
		ModelTester tester = Action.create(ModelTester.class);
		tester.test();
	}

	public static void main(String[] args) {
		new Tester().run();
	}

}
