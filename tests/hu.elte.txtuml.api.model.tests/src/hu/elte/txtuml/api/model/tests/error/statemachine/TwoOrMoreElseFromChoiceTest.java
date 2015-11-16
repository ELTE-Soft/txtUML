package hu.elte.txtuml.api.model.tests.error.statemachine;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.tests.base.TestsBase;
import hu.elte.txtuml.api.model.tests.models.erronous.multipleelse.A;
import hu.elte.txtuml.api.model.tests.models.erronous.multipleelse.Sig;
import hu.elte.txtuml.api.model.tests.util.SeparateClassloaderTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class TwoOrMoreElseFromChoiceTest extends TestsBase {

	@Test
	public void test() {

		A a = new A();
		Action.start(a);
		Action.send(a, new Sig());

		stopModelExecution();

		executionAsserter.assertErrors(x -> x
				.moreThanOneElseTransitionsFromChoice(a.new C()));

	}
}
