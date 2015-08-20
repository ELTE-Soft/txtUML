package hu.elte.txtuml.api.model.tests.statemachine;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.tests.base.TransitionsModelTestsBase;
import hu.elte.txtuml.api.model.tests.models.TransitionsModel.Sig3;
import hu.elte.txtuml.api.model.tests.util.SeparateClassloaderTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class GuardTest extends TransitionsModelTestsBase {

	@Test
	public void test() { 
		Action.send(a, new Sig3());
		Action.send(a, new Sig3());
		Action.send(a, new Sig3());
		Action.send(a, new Sig3());

		stopModelExecution();
		
		/*
		Assert.assertArrayEquals(
				new String[] { 
						LogMessages.getUsingTransitionMessage(a, a.new Initialize()),
						LogMessages.getProcessingSignalMessage(a, new Sig3()),
						LogMessages.getUsingTransitionMessage(a, a.new T3()),
						LogMessages.getProcessingSignalMessage(a, new Sig3()),
						LogMessages.getUsingTransitionMessage(a, a.new T4()),
						LogMessages.getProcessingSignalMessage(a, new Sig3()),
						LogMessages.getUsingTransitionMessage(a, a.new T3()),
						LogMessages.getProcessingSignalMessage(a, new Sig3()),
						LogMessages.getUsingTransitionMessage(a, a.new T4()),
						LogMessages.getModelExecutionShutdownMessage() },
				executorStream.getOutputAsArray());
		*/
	}
}
