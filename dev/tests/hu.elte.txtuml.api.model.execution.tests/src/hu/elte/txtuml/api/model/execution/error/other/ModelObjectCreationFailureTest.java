package hu.elte.txtuml.api.model.execution.error.other;

import org.junit.Assert;
import org.junit.Test;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.error.ObjectCreationError;
import hu.elte.txtuml.api.model.execution.base.TestsBase;
import hu.elte.txtuml.api.model.execution.models.simple.A;
import hu.elte.txtuml.api.model.execution.util.MutableBoolean;

public class ModelObjectCreationFailureTest extends TestsBase {

	@Test
	public void test() {

		MutableBoolean bool = new MutableBoolean(false);

		executor.run(() -> {
			try {
				Action.create(A.class, 100);
			} catch (ObjectCreationError e) {
				bool.value = true;
			}
		});
		
		Assert.assertTrue(bool.value);
	}

}
