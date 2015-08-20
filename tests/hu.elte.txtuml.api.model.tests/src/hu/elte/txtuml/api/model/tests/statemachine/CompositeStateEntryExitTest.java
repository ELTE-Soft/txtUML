package hu.elte.txtuml.api.model.tests.statemachine;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.tests.base.HierarchicalModelTestsBase;
import hu.elte.txtuml.api.model.tests.models.HierarchicalModel.Sig0;
import hu.elte.txtuml.api.model.tests.models.HierarchicalModel.Sig1;
import hu.elte.txtuml.api.model.tests.util.SeparateClassloaderTestRunner;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class CompositeStateEntryExitTest extends HierarchicalModelTestsBase {

	@Test
	public void test() {
		Action.send(a, new Sig0());
		Action.send(a, new Sig0());
		Action.send(a, new Sig1());

		stopModelExecution();

		Assert.assertArrayEquals(new String[]
				{ "CS1 entry", "CS2 entry",	"S3 entry", "S3 exit", "CS2 exit", "CS1 exit" },
				userStream.getOutputAsArray());
	}

}
