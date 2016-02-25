package hu.elte.txtuml.api.model.execution.error.statemachine;

import org.junit.Test;
import org.junit.runner.RunWith;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.base.TestsBase;
import hu.elte.txtuml.api.model.execution.models.erronous.notransitionfromchoice.A;
import hu.elte.txtuml.api.model.execution.models.erronous.notransitionfromchoice.Sig;
import hu.elte.txtuml.api.model.execution.util.SeparateClassloaderTestRunner;

@RunWith(SeparateClassloaderTestRunner.class)
public class NoTransitionsFromChoiceTest extends TestsBase {

	@Test
	public void test() {

		A a = new A();
		Action.start(a);
		Action.send(new Sig(2), a);

		stopModelExecution();

		executionAsserter
				.assertErrors(x -> x.noTransitionFromChoice(a.new C()));
	}
}
