package hu.elte.txtuml.api.model.error.statemachine;

import org.junit.Test;
import org.junit.runner.RunWith;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.base.TestsBase;
import hu.elte.txtuml.api.model.models.erronous.multipleelse.A;
import hu.elte.txtuml.api.model.models.erronous.multipleelse.Sig;
import hu.elte.txtuml.api.model.util.SeparateClassloaderTestRunner;

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
