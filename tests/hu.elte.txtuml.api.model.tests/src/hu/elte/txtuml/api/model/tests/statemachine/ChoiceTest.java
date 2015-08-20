package hu.elte.txtuml.api.model.tests.statemachine;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelInt;
import hu.elte.txtuml.api.model.tests.base.ChoiceModelTestsBase;
import hu.elte.txtuml.api.model.tests.models.ChoiceModel.Sig;
import hu.elte.txtuml.api.model.tests.util.SeparateClassloaderTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class ChoiceTest extends ChoiceModelTestsBase {

	@Test
	public void test() {
		Action.send(a, new Sig(new ModelInt(0)));
		Action.send(a, new Sig(new ModelInt(1)));
		Action.send(a, new Sig(new ModelInt(2)));
		
		stopModelExecution();
		
		/*
		Assert.assertArrayEquals(
				new String[] { 
						LogMessages.getUsingTransitionMessage(a, a.new Initialize()),
						LogMessages.getProcessingSignalMessage(a, new Sig()),
						LogMessages.getUsingTransitionMessage(a, a.new S1_C()),
						LogMessages.getUsingTransitionMessage(a, a.new T1()),
						LogMessages.getProcessingSignalMessage(a, new Sig()),
						LogMessages.getUsingTransitionMessage(a, a.new S1_C()),
						LogMessages.getUsingTransitionMessage(a, a.new T2()),
						LogMessages.getProcessingSignalMessage(a, new Sig()),
						LogMessages.getUsingTransitionMessage(a, a.new S1_C()),
						LogMessages.getUsingTransitionMessage(a, a.new T3()),
						LogMessages.getModelExecutionShutdownMessage() },
				executorStream.getOutputAsArray());
		*/
	}
	
}