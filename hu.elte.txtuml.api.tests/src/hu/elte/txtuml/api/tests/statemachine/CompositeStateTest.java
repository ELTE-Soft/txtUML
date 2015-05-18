package hu.elte.txtuml.api.tests.statemachine;

import hu.elte.txtuml.api.Action;
import hu.elte.txtuml.api.backend.messages.LogMessages;
import hu.elte.txtuml.api.tests.base.HierarchicalModelTestsBase;
import hu.elte.txtuml.api.tests.models.HierarchicalModel.Sig0;
import hu.elte.txtuml.api.tests.models.HierarchicalModel.Sig1;
import hu.elte.txtuml.api.tests.util.SeparateClassloaderTestRunner;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class CompositeStateTest extends HierarchicalModelTestsBase {

	@Test
	public void test() {
		Action.send(a, new Sig0());
		Action.send(a, new Sig0());
		Action.send(a, new Sig1());
		
		stopModelExecution();
		
		Assert.assertArrayEquals(
				new String[] { 
						LogMessages.getUsingTransitionMessage(a, a.new Initialize()),
						LogMessages.getProcessingSignalMessage(a, new Sig0()),
						LogMessages.getUsingTransitionMessage(a, a.new S1_CS1()),
						LogMessages.getEnteringVertexMessage(a, a.new CS1()),
						LogMessages.getUsingTransitionMessage(a, a.new CS1().new Initialize()),
						LogMessages.getProcessingSignalMessage(a, new Sig0()),
						LogMessages.getUsingTransitionMessage(a, a.new CS1().new S2_CS2()),
						LogMessages.getEnteringVertexMessage(a, a.new CS1().new CS2()),
						LogMessages.getUsingTransitionMessage(a, a.new CS1().new CS2().new Initialize()),
						LogMessages.getProcessingSignalMessage(a, new Sig1()),
						LogMessages.getUsingTransitionMessage(a, a.new CS1_S1()),
						LogMessages.getLeavingVertexMessage(a, a.new CS1().new CS2()),
						LogMessages.getLeavingVertexMessage(a, a.new CS1()),
						LogMessages.getModelExecutionShutdownMessage() },
				executorStream.getOutputAsArray());
	}
	
}
