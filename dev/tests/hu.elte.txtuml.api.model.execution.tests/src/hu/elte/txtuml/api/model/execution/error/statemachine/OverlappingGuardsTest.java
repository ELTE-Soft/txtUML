package hu.elte.txtuml.api.model.execution.error.statemachine;

import org.junit.Assert;
import org.junit.Test;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.base.TestsBase;
import hu.elte.txtuml.api.model.execution.models.erronous.overlapping.A;
import hu.elte.txtuml.api.model.execution.models.erronous.overlapping.Sig;

public class OverlappingGuardsTest extends TestsBase {

	private A a;
	
	@Test
	public void test() {

		executor.run(() -> {
			a = new A();
			Action.start(a);
			Action.send(new Sig(), a);
		});

		boolean msg1 = executionAsserter
				.checkErrors(x -> x.guardsOfTransitionsAreOverlapping(a.new T1(), a.new T2(), a.new S1()));
		boolean msg2 = executionAsserter
				.checkErrors(x -> x.guardsOfTransitionsAreOverlapping(a.new T2(), a.new T1(), a.new S1()));

		Assert.assertTrue(msg1 || msg2);

	}

}
