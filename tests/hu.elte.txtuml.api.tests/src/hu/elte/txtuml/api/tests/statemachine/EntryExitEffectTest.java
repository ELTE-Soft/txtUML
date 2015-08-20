package hu.elte.txtuml.api.tests.statemachine;

import hu.elte.txtuml.api.Action;
import hu.elte.txtuml.api.tests.base.TransitionsModelTestsBase;
import hu.elte.txtuml.api.tests.models.TransitionsModel.Sig1;
import hu.elte.txtuml.api.tests.models.TransitionsModel.Sig2;
import hu.elte.txtuml.api.tests.util.SeparateClassloaderTestRunner;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class EntryExitEffectTest extends TransitionsModelTestsBase {

	@Test
	public void test() {
		Action.send(a, new Sig1());
		Action.send(a, new Sig2());

		stopModelExecution();
		
		Assert.assertArrayEquals(
				new String[] { 
						"entry", "exit", "T1", "entry", "exit", "T2", "entry" },
				userStream.getOutputAsArray());
	}
}
