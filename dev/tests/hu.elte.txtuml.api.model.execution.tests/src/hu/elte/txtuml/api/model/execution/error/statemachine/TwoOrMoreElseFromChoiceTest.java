package hu.elte.txtuml.api.model.execution.error.statemachine;

import org.junit.Test;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.base.TestsBase;
import hu.elte.txtuml.api.model.execution.models.erronous.multipleelse.A;
import hu.elte.txtuml.api.model.execution.models.erronous.multipleelse.Sig;

public class TwoOrMoreElseFromChoiceTest extends TestsBase {

	private A a;

	@Test
	public void test() {

		executor.run(() -> {
			a = new A();
			Action.start(a);
			Action.send(new Sig(), a);
		});

		executionAsserter.assertErrors(x -> x.moreThanOneElseTransitionsFromChoice(a.new C()));

	}
}
