package hu.elte.txtuml.api.model.execution.unittests;

import org.junit.Assert;
import org.junit.Test;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.error.ObjectCreationError;
import hu.elte.txtuml.api.model.execution.testmodel.A;
import hu.elte.txtuml.api.model.execution.testmodel.assoc.A_B;
import hu.elte.txtuml.api.model.execution.util.MutableBoolean;

public class OtherErrorTests extends UnitTestsBase {
	@Test
	public void testModelObjectCreationFailure() {

		MutableBoolean bool = new MutableBoolean(false);

		executor.run(() -> {
			try {
				Action.create(A.class, 100);
			} catch (ObjectCreationError e) {
				bool.value = true;
			}
		});

		Assert.assertTrue(bool.value);
		assertNoErrors();
		assertNoWarnings();
	}

	@Test
	public void testUnlinkingNonExistingAssociation() {

		executor.run(() -> {
			createAAndB();
			Action.unlink(A_B.a.class, a, A_B.b.class, b);
		});

		assertNoErrors();
		assertWarnings(x -> x.unlinkingNonExistingAssociation(A_B.a.class, a, A_B.b.class, b));
	}

}
