package hu.elte.txtuml.api.model.execution.impl.seqdiag;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.StateMachine.Transition;
import hu.elte.txtuml.api.model.StateMachine.Vertex;
import hu.elte.txtuml.api.model.execution.TraceListener;

public class CommunicationListener extends AbstractSequenceDiagramModelListener implements TraceListener  {
	
	public CommunicationListener(SequenceDiagramExecutor executor)
	{
		super(executor);
	}
	
	public void executionStarted() {
	}

	public void processingSignal(ModelClass object, Signal signal) {
	}

	public void usingTransition(ModelClass object, Transition transition) {
	}

	public void enteringVertex(ModelClass object, Vertex vertex) {
	}

	public void leavingVertex(ModelClass object, Vertex vertex) {
	}

	public void executionTerminated() {
	}
}
