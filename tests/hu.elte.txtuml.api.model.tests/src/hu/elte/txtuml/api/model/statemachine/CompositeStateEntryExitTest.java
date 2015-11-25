package hu.elte.txtuml.api.model.statemachine;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.base.HierarchicalModelTestsBase;
import hu.elte.txtuml.api.model.tests.models.hierarchical.Sig0;
import hu.elte.txtuml.api.model.tests.models.hierarchical.Sig1;
import hu.elte.txtuml.api.model.util.SeparateClassloaderTestRunner;

@RunWith(SeparateClassloaderTestRunner.class)
public class CompositeStateEntryExitTest extends HierarchicalModelTestsBase {

	@Test
	public void test() {
		Action.send(a, new Sig0());
		Action.send(a, new Sig0());
		Action.send(a, new Sig1());

		stopModelExecution();

		Assert.assertArrayEquals(new String[] { "CS1 entry", "CS2 entry",
				"S3 entry", "S3 exit", "CS2 exit", "CS1 exit" },
				userOutStream.getOutputAsArray());
	}

}
