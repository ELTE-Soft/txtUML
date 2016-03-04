package hu.elte.txtuml.api.model.error.deletion;

import org.junit.Test;
import org.junit.runner.RunWith;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.model.models.simple.Sig;
import hu.elte.txtuml.api.model.util.SeparateClassloaderTestRunner;

@RunWith(SeparateClassloaderTestRunner.class)
public class SignalArrivingToDeletedTest extends SimpleModelTestsBase {
	
	@Test
	public void test() {
		
		Action.delete(a);
		Action.send(new Sig(), a);
		
		stopModelExecution();

		executionAsserter.assertWarnings( x -> x.signalArrivedToDeletedObject(a, new Sig()));

	}

}
