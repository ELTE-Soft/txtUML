package hu.elte.txtuml.api.model.execution.statemachine;

import org.junit.Test;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.base.TransitionsModelTestsBase;
import hu.elte.txtuml.api.model.execution.models.transitions.Sig3;

public class GuardTest extends TransitionsModelTestsBase {

	@Test
	public void test() {
		executor.run(() -> {
			createAndStartA();
			Action.send(new Sig3(), a);
			Action.send(new Sig3(), a);
			Action.send(new Sig3(), a);
			Action.send(new Sig3(), a);
		});

		executionAsserter.assertEvents(x -> {
			x.executionStarted();
			transition(x, a, a.new Initialize());
			x.processingSignal(a, new Sig3());
			transition(x, a, a.new T3());
			x.processingSignal(a, new Sig3());
			transition(x, a, a.new T4());
			x.processingSignal(a, new Sig3());
			transition(x, a, a.new T3());
			x.processingSignal(a, new Sig3());
			transition(x, a, a.new T4());
			x.executionTerminated();
		});
	}
}
