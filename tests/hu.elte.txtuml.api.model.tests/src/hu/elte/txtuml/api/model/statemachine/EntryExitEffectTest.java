package hu.elte.txtuml.api.model.statemachine;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.base.TransitionsModelTestsBase;
import hu.elte.txtuml.api.model.models.TransitionsModel.Sig1;
import hu.elte.txtuml.api.model.models.TransitionsModel.Sig2;
import hu.elte.txtuml.api.model.util.SeparateClassloaderTestRunner;

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
				userOutStream.getOutputAsArray());
	}
}
