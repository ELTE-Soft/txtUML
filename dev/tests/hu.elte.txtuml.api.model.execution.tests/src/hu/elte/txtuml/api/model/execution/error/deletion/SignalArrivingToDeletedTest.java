package hu.elte.txtuml.api.model.execution.error.deletion;

import org.junit.Test;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.model.execution.models.simple.Sig;

public class SignalArrivingToDeletedTest extends SimpleModelTestsBase {

	@Test
	public void test() {
		executor.run(() -> {
			createAAndB();
			Action.delete(a);
			Action.send(new Sig(), a);
		});

		executionAsserter.assertWarnings(x -> x.signalArrivedToDeletedObject(a, new Sig()));

	}

}
