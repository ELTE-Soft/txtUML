package hu.elte.txtuml.api.model.execution.unittests;

import org.junit.Assert;
import org.junit.Test;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.testmodel.A;
import hu.elte.txtuml.api.model.execution.testmodel.Part;
import hu.elte.txtuml.api.model.execution.testmodel.assoc.A_Part;
import hu.elte.txtuml.utils.Reference;

public class CompositionTests extends UnitTestsBase {
	@Test
	public void tryComposition() throws Exception {
		Reference<Integer> count = Reference.empty();

		executor.run(() -> {
			A a = Action.create(A.class);
			Action.link(A_Part.a.class, a, A_Part.p.class, Action.create(Part.class));
			Action.link(A_Part.a.class, a, A_Part.p.class, Action.create(Part.class));
			Action.link(A_Part.a.class, a, A_Part.p.class, Action.create(Part.class));
			count.set(a.assoc(A_Part.p.class).size());
		});

		Assert.assertEquals(Integer.valueOf(3), count.get());
		assertNoErrors();
		assertNoWarnings();
	}

	@Test
	public void testCompositionValidate() {
		Reference<Part> part = Reference.empty();

		executor.run(() -> {
			A a = Action.create(A.class);
			Part p = Action.create(Part.class);
			part.set(p);

			Action.link(A_Part.a.class, a, A_Part.p.class, p);
			Action.link(A_Part.a.class, a, A_Part.p.class, p);
		});

		assertErrors(x -> x.multipleContainerForAnObject(part.get(), A_Part.a.class));
		assertNoWarnings();
	}

}
