package hu.elte.txtuml.api.model.backend.log;

import hu.elte.txtuml.api.model.Region;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.StateMachine.CompositeState;
import hu.elte.txtuml.api.model.StateMachine.Transition;
import hu.elte.txtuml.api.model.report.ModelExecutionEventsListener;

class ModelExecutionEventsListenerImpl extends BaseListenerImpl
		implements ModelExecutionEventsListener {

	ModelExecutionEventsListenerImpl(ExecutorLog log) {
		super(log);
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
	public void enteringCompositeState(Region region, CompositeState state) {
		out(region + " enters " + state);
	}

	@Override
	public void leavingCompositeState(Region region, CompositeState state) {
		out(region + " leaves " + state);
	}
	
	@Override
	public void executionTerminated() {
		out("Model execution terminated.");
	}	

}
