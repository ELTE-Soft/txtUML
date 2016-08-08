package hu.elte.txtuml.api.model.execution.impl.seqdiag;

import java.util.LinkedList;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.StateMachine.Transition;
import hu.elte.txtuml.api.model.StateMachine.Vertex;
import hu.elte.txtuml.api.model.error.seqdiag.InvalidMessageError;
import hu.elte.txtuml.api.model.execution.TraceListener;
import hu.elte.txtuml.api.model.seqdiag.ImprintedListener;

public class CommunicationListener extends AbstractSequenceDiagramModelListener implements TraceListener,ImprintedListener  {
	
	protected LinkedList<Signal> suggestedMessagePattern; 
	
	public CommunicationListener(SequenceDiagramExecutor executor)
	{
		super(executor);
		suggestedMessagePattern = new LinkedList<Signal>();
	}
	
	public void executionStarted() {
	}

	public void processingSignal(ModelClass object, Signal signal) {
		if(suggestedMessagePattern.size() > 0)
		{	
			Signal required = suggestedMessagePattern.poll();
			
			if(!signal.equals(required))
			{
				executor.addError(new InvalidMessageError(object,"The model diverged from the Sequence-diagram Specified behaviour:\n it sent: " + signal.toString() + " instead of " + required.toString() + "\n" ));
			}
		}
		else
		{
			executor.addError(new InvalidMessageError(object,"The model sent more signals than the pattern ovelapped" ));
		}
	}
	
	public void addToPattern(Signal sig)
	{
		this.suggestedMessagePattern.add(sig);
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
