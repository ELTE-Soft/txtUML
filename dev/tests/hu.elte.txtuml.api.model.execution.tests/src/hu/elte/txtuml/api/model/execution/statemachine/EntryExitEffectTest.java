package hu.elte.txtuml.api.model.execution.statemachine;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Matchers;
import org.mockito.Mockito;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelExecutor;
import hu.elte.txtuml.api.model.execution.base.TransitionsModelTestsBase;
import hu.elte.txtuml.api.model.execution.models.transitions.A;
import hu.elte.txtuml.api.model.execution.models.transitions.Sig1;
import hu.elte.txtuml.api.model.execution.models.transitions.Sig2;
import hu.elte.txtuml.api.model.execution.util.SeparateClassloaderTestRunner;
import hu.elte.txtuml.api.model.report.ModelExecutionEventsListener;

// TODO This test should explicitly check whether the entry and exit methods are called in the model, not that it is reported.
@RunWith(SeparateClassloaderTestRunner.class)
public class EntryExitEffectTest extends TransitionsModelTestsBase {

	@Test
	public void test() {
		ModelExecutionEventsListener mock = Mockito.mock(ModelExecutionEventsListener.class);

		ModelExecutor.Report.addModelExecutionEventsListener(mock);

		Action.send(new Sig1(), a);
		Action.send(new Sig2(), a);

		stopModelExecution();

		InOrder inOrder = Mockito.inOrder(mock);
		inOrder.verify(mock).enteringVertex(Matchers.isA(A.class), Matchers.isA(A.S.class));
		inOrder.verify(mock).leavingVertex(Matchers.isA(A.class), Matchers.isA(A.S.class));
		inOrder.verify(mock).usingTransition(Matchers.isA(A.class), Matchers.isA(A.T1.class));
		inOrder.verify(mock).enteringVertex(Matchers.isA(A.class), Matchers.isA(A.S.class));
		inOrder.verify(mock).leavingVertex(Matchers.isA(A.class), Matchers.isA(A.S.class));
		inOrder.verify(mock).usingTransition(Matchers.isA(A.class), Matchers.isA(A.T2.class));
		inOrder.verify(mock).enteringVertex(Matchers.isA(A.class), Matchers.isA(A.S.class));
	}
}
