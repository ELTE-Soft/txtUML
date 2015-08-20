package hu.elte.txtuml.api.model.tests.statemachine;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.tests.base.TransitionsModelTestsBase;
import hu.elte.txtuml.api.model.tests.models.TransitionsModel.Sig1;
import hu.elte.txtuml.api.model.tests.models.TransitionsModel.Sig2;
import hu.elte.txtuml.api.model.tests.util.SeparateClassloaderTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class TriggerTest extends TransitionsModelTestsBase {

	@Test
	public void test() { 
		Action.send(a, new Sig1());
		Action.send(a, new Sig2());
		Action.send(a, new Sig1());
		Action.send(a, new Sig1());
		Action.send(a, new Sig1());
		Action.send(a, new Sig2());

		stopModelExecution();
		
		/*
		Assert.assertArrayEquals(
				new String[] { 
						LogMessages.getUsingTransitionMessage(a, a.new Initialize()),
						LogMessages.getProcessingSignalMessage(a, new Sig1()),
						LogMessages.getUsingTransitionMessage(a, a.new T1()),
						LogMessages.getProcessingSignalMessage(a, new Sig2()),
						LogMessages.getUsingTransitionMessage(a, a.new T2()),
						LogMessages.getProcessingSignalMessage(a, new Sig1()),
						LogMessages.getUsingTransitionMessage(a, a.new T1()),
						LogMessages.getProcessingSignalMessage(a, new Sig1()),
						LogMessages.getUsingTransitionMessage(a, a.new T1()),
						LogMessages.getProcessingSignalMessage(a, new Sig1()),
						LogMessages.getUsingTransitionMessage(a, a.new T1()),
						LogMessages.getProcessingSignalMessage(a, new Sig2()),
						LogMessages.getUsingTransitionMessage(a, a.new T2()),
						LogMessages.getModelExecutionShutdownMessage() },
				executorStream.getOutputAsArray());
		*/
	}
}
