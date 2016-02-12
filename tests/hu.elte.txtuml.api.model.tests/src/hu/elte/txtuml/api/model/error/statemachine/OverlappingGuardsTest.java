package hu.elte.txtuml.api.model.error.statemachine;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.base.TestsBase;
import hu.elte.txtuml.api.model.models.erronous.overlapping.A;
import hu.elte.txtuml.api.model.models.erronous.overlapping.Sig;
import hu.elte.txtuml.api.model.util.SeparateClassloaderTestRunner;

@RunWith(SeparateClassloaderTestRunner.class)
public class OverlappingGuardsTest extends TestsBase {

	@Test
	public void test() {

		A a = new A();
		Action.start(a);
		Action.send(a, new Sig());

		stopModelExecution();

		boolean msg1 = executionAsserter.checkErrors(x -> x
				.guardsOfTransitionsAreOverlapping(a.new T1(), a.new T2(),
						a.new S1()));
		boolean msg2 = executionAsserter.checkErrors(x -> x
				.guardsOfTransitionsAreOverlapping(a.new T2(), a.new T1(),
						a.new S1()));

		Assert.assertTrue(msg1 || msg2);

	}

}
