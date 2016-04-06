package hu.elte.txtuml.api.model.execution.error.statemachine;

import org.junit.Test;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.base.TestsBase;
import hu.elte.txtuml.api.model.execution.models.erronous.notransitionfromchoice.A;
import hu.elte.txtuml.api.model.execution.models.erronous.notransitionfromchoice.Sig;

public class NoTransitionsFromChoiceTest extends TestsBase {

	private A a;
	
	@Test
	public void test() {

		executor.run(() -> {
			a = new A();
			Action.start(a);
			Action.send(new Sig(2), a);
		});

		executionAsserter.assertErrors(x -> x.noTransitionFromChoice(a.new C()));
	}
}
