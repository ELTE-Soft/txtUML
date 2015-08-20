package hu.elte.txtuml.api.model.tests.error.multiplicity;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.tests.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.model.tests.util.SeparateClassloaderTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class LowerBoundInitiallyOffendedTest extends SimpleModelTestsBase {

	@Test
	public void test() {
		Action.start(a);
		
		stopModelExecution();

		/*
		Assert.assertArrayEquals(
				new String[] { ErrorMessages.getLowerBoundOfMultiplicityOffendedMessage(a, A_B.b.class) },
				executorErrorStream.getOutputAsArray());
		*/
	}

}
