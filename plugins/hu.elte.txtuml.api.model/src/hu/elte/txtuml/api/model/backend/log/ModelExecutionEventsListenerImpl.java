package hu.elte.txtuml.api.model.backend.log;

import hu.elte.txtuml.api.model.Region;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.StateMachine.Vertex;
import hu.elte.txtuml.api.model.StateMachine.Transition;
import hu.elte.txtuml.api.model.report.ModelExecutionEventsListener;

final class ModelExecutionEventsListenerImpl extends BaseListenerImpl
		implements ModelExecutionEventsListener {

	ModelExecutionEventsListenerImpl(ExecutorLog owner) {
		super(owner);
	}

	@Override
	public void processingSignal(Region region, Signal signal) {
		out(region + " processes " + signal);
	}

	@Override
	public void usingTransition(Region region, Transition transition) {
		out(region + " uses " + transition);
	}

	@Override
	public void enteringVertex(Region region, Vertex vertex) {
		out(region + " enters " + vertex);
	}

	@Override
	public void leavingVertex(Region region, Vertex vertex) {
		out(region + " leaves " + vertex);
	}
	
	@Override
	public void executionTerminated() {
		out("Model execution terminated.");
	}	

}
