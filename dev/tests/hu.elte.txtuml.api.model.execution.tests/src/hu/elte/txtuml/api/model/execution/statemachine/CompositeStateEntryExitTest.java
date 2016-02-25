package hu.elte.txtuml.api.model.execution.statemachine;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Matchers;
import org.mockito.Mockito;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelExecutor;
import hu.elte.txtuml.api.model.execution.base.HierarchicalModelTestsBase;
import hu.elte.txtuml.api.model.execution.models.hierarchical.A;
import hu.elte.txtuml.api.model.execution.models.hierarchical.Sig0;
import hu.elte.txtuml.api.model.execution.models.hierarchical.Sig1;
import hu.elte.txtuml.api.model.execution.util.SeparateClassloaderTestRunner;
import hu.elte.txtuml.api.model.report.ModelExecutionEventsListener;

//TODO This test should explicitly check whether the entry and exit methods are called in the model, not that it is reported.
@RunWith(SeparateClassloaderTestRunner.class)
public class CompositeStateEntryExitTest extends HierarchicalModelTestsBase {

	@Test
	public void test() {
		ModelExecutionEventsListener mock = Mockito.mock(ModelExecutionEventsListener.class);
		ModelExecutor.Report.addModelExecutionEventsListener(mock);
		
		Action.send(new Sig0(), a);
		Action.send(new Sig0(), a);
		Action.send(new Sig1(), a);

		stopModelExecution();

		InOrder inOrder = Mockito.inOrder(mock);
		inOrder.verify(mock).enteringVertex(Matchers.isA(A.class), Matchers.isA(A.CS1.class));
		inOrder.verify(mock).enteringVertex(Matchers.isA(A.class), Matchers.isA(A.CS1.CS2.class));
		inOrder.verify(mock).enteringVertex(Matchers.isA(A.class), Matchers.isA(A.CS1.CS2.S3.class));
		inOrder.verify(mock).leavingVertex(Matchers.isA(A.class), Matchers.isA(A.CS1.CS2.S3.class));
		inOrder.verify(mock).leavingVertex(Matchers.isA(A.class), Matchers.isA(A.CS1.CS2.class));
		inOrder.verify(mock).leavingVertex(Matchers.isA(A.class), Matchers.isA(A.CS1.class));
	}

}
