package hu.elte.txtuml.api.model.statemachine;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.base.ChoiceModelTestsBase;
import hu.elte.txtuml.api.model.models.ChoiceModel.Sig;
import hu.elte.txtuml.api.model.util.SeparateClassloaderTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class ChoiceTest extends ChoiceModelTestsBase {

	@Test
	public void test() {
		Action.send(a, new Sig(0));
		Action.send(a, new Sig(1));
		Action.send(a, new Sig(2));
		
		stopModelExecution();
		
		executionAsserter.assertEvents( x -> {
			transition(x, a, a.new Initialize());
			x.processingSignal(a, new Sig());
			transition(x, a, a.new S1_C());
			transition(x, a, a.new T1());
			x.processingSignal(a, new Sig());
			transition(x, a, a.new S1_C());
			transition(x, a, a.new T2());
			x.processingSignal(a, new Sig());
			transition(x, a, a.new S1_C());
			transition(x, a, a.new T3());
			x.executionTerminated();
		});

	}
}