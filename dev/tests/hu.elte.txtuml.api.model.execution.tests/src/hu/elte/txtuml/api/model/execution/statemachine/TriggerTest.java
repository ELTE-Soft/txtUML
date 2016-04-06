package hu.elte.txtuml.api.model.execution.statemachine;

import org.junit.Test;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.base.TransitionsModelTestsBase;
import hu.elte.txtuml.api.model.execution.models.transitions.Sig1;
import hu.elte.txtuml.api.model.execution.models.transitions.Sig2;

public class TriggerTest extends TransitionsModelTestsBase {

	@Test
	public void test() {
		executor.setTraceLogging(true).run(() -> {
			createAndStartA();
			Action.send(new Sig1(), a);
			Action.send(new Sig2(), a);
			Action.send(new Sig1(), a);
			Action.send(new Sig1(), a);
			Action.send(new Sig1(), a);
			Action.send(new Sig2(), a);
		});

		executionAsserter.assertEvents(x -> {
			x.executionStarted();
			transition(x, a, a.new Initialize());
			x.processingSignal(a, new Sig1());
			transition(x, a, a.new T1());
			x.processingSignal(a, new Sig2());
			transition(x, a, a.new T2());
			x.processingSignal(a, new Sig1());
			transition(x, a, a.new T1());
			x.processingSignal(a, new Sig1());
			transition(x, a, a.new T1());
			x.processingSignal(a, new Sig1());
			transition(x, a, a.new T1());
			x.processingSignal(a, new Sig2());
			transition(x, a, a.new T2());
			x.executionTerminated();
		});
	}
}
