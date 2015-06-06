package hu.elte.txtuml.api.tests.error.other;

import hu.elte.txtuml.api.Action;
import hu.elte.txtuml.api.backend.messages.WarningMessages;
import hu.elte.txtuml.api.tests.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.tests.models.SimpleModel.A_B;
import hu.elte.txtuml.api.tests.util.SeparateClassloaderTestRunner;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class UnlinkingNonExistingAssociationTest extends SimpleModelTestsBase {

	@Test
	public void test() {

		Action.unlink(A_B.a.class, a, A_B.b.class, b);

		stopModelExecution();

		Assert.assertArrayEquals(new String[] { WarningMessages
				.getUnlinkingNonExistingAssociationMessage(a, b) },
				executorErrorStream.getOutputAsArray());

	}

}
