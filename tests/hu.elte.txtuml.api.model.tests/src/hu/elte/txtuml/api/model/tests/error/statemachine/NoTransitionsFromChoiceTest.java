package hu.elte.txtuml.api.model.tests.error.statemachine;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.tests.base.TestsBase;
import hu.elte.txtuml.api.model.tests.models.erronous.notransitionfromchoice.A;
import hu.elte.txtuml.api.model.tests.models.erronous.notransitionfromchoice.Sig;
import hu.elte.txtuml.api.model.tests.util.SeparateClassloaderTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class NoTransitionsFromChoiceTest extends TestsBase {

	@Test
	public void test() {

		A a = new A();
		Action.start(a);
		Action.send(a, new Sig(2));

		stopModelExecution();

		executionAsserter
				.assertErrors(x -> x.noTransitionFromChoice(a.new C()));
	}
}
