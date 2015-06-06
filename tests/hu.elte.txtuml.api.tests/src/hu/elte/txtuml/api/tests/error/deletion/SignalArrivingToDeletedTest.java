package hu.elte.txtuml.api.tests.error.deletion;

import hu.elte.txtuml.api.Action;
import hu.elte.txtuml.api.backend.messages.WarningMessages;
import hu.elte.txtuml.api.tests.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.tests.models.SimpleModel.Sig;
import hu.elte.txtuml.api.tests.util.SeparateClassloaderTestRunner;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class SignalArrivingToDeletedTest extends SimpleModelTestsBase {
	
	@Test
	public void test() {
		
		Action.delete(a);
		Action.send(a, new Sig());
		
		stopModelExecution();

		Assert.assertArrayEquals(
				new String[] { WarningMessages.getSignalArrivedToDeletedObjectMessage(a) },
				executorErrorStream.getOutputAsArray());

	}

}
