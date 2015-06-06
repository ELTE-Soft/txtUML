package hu.elte.txtuml.api.tests.error.multiplicity;

import hu.elte.txtuml.api.Action;
import hu.elte.txtuml.api.backend.messages.ErrorMessages;
import hu.elte.txtuml.api.tests.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.tests.models.SimpleModel.A_B;
import hu.elte.txtuml.api.tests.models.SimpleModel.B;
import hu.elte.txtuml.api.tests.util.SeparateClassloaderTestRunner;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class UpperBoundOffendedTest extends SimpleModelTestsBase {
	
	@Test
	public void test() {
		Action.link(A_B.a.class, a, A_B.b.class, b);
		B b2 = new B();
		Action.link(A_B.a.class, a, A_B.b.class, b2);
		
		stopModelExecution();

		Assert.assertArrayEquals(
				new String[] { ErrorMessages.getUpperBoundOfMultiplicityOffendedMessage(a, A_B.b.class) },
				executorErrorStream.getOutputAsArray());

	}
}
