package hu.elte.txtuml.api.model.tests.error.deletion;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.tests.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.model.tests.models.simple.Sig;
import hu.elte.txtuml.api.model.tests.util.SeparateClassloaderTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class SignalArrivingToDeletedTest extends SimpleModelTestsBase {
	
	@Test
	public void test() {
		
		Action.delete(a);
		Action.send(a, new Sig());
		
		stopModelExecution();

		executionAsserter.assertWarnings( x -> x.signalArrivedToDeletedObject(a, new Sig()));

	}

}
