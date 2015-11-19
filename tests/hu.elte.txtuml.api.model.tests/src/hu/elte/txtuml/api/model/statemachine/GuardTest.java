package hu.elte.txtuml.api.model.statemachine;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.base.TransitionsModelTestsBase;
import hu.elte.txtuml.api.model.models.TransitionsModel.Sig3;
import hu.elte.txtuml.api.model.util.SeparateClassloaderTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class GuardTest extends TransitionsModelTestsBase {

	@Test
	public void test() {
		Action.send(a, new Sig3());
		Action.send(a, new Sig3());
		Action.send(a, new Sig3());
		Action.send(a, new Sig3());

		stopModelExecution();

		executionAsserter.assertEvents(x -> {
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
