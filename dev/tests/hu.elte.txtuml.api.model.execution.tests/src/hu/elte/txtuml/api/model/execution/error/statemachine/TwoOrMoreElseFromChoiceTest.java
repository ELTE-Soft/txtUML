package hu.elte.txtuml.api.model.execution.error.statemachine;

import org.junit.Test;
import org.junit.runner.RunWith;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.base.TestsBase;
import hu.elte.txtuml.api.model.execution.models.erronous.multipleelse.A;
import hu.elte.txtuml.api.model.execution.models.erronous.multipleelse.Sig;
import hu.elte.txtuml.api.model.execution.util.SeparateClassloaderTestRunner;

@RunWith(SeparateClassloaderTestRunner.class)
public class TwoOrMoreElseFromChoiceTest extends TestsBase {

	@Test
	public void test() {

		A a = new A();
		Action.start(a);
		Action.send(new Sig(), a);

		stopModelExecution();

		executionAsserter.assertErrors(x -> x
				.moreThanOneElseTransitionsFromChoice(a.new C()));

	}
}
