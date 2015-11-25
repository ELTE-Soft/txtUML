package hu.elte.txtuml.api.model.error.deletion;

import org.junit.Test;
import org.junit.runner.RunWith;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.base.SimpleModelTestsBase;
import hu.elte.txtuml.api.model.models.simple.A_B;
import hu.elte.txtuml.api.model.util.SeparateClassloaderTestRunner;

@RunWith(SeparateClassloaderTestRunner.class)
public class LinkingDeletedTest extends SimpleModelTestsBase {

	@Test
	public void test() {

		Action.delete(a);
		Action.link(A_B.a.class, a, A_B.b.class, b);

		stopModelExecution();

		executionAsserter.assertErrors(x -> x.linkingDeletedObject(a));

	}

}
