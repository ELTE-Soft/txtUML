package hu.elte.txtuml.api.model.statemachine;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Matchers;
import org.mockito.Mockito;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelExecutor;
import hu.elte.txtuml.api.model.base.TransitionsModelTestsBase;
import hu.elte.txtuml.api.model.models.transitions.A;
import hu.elte.txtuml.api.model.models.transitions.Sig1;
import hu.elte.txtuml.api.model.models.transitions.Sig2;
import hu.elte.txtuml.api.model.report.ModelExecutionEventsListener;
import hu.elte.txtuml.api.model.util.SeparateClassloaderTestRunner;

@RunWith(SeparateClassloaderTestRunner.class)
public class EntryExitEffectTest extends TransitionsModelTestsBase {

	@Test
	public void test() {
		ModelExecutionEventsListener mock = Mockito.mock(ModelExecutionEventsListener.class);

		ModelExecutor.Report.addModelExecutionEventsListener(mock);

		Action.send(a, new Sig1());
		Action.send(a, new Sig2());

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
