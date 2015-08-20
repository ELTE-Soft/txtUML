package hu.elte.txtuml.api.model.report;

import hu.elte.txtuml.api.model.Region;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.StateMachine.CompositeState;
import hu.elte.txtuml.api.model.StateMachine.Transition;

public interface ModelExecutionEventsListener {
	
	default void processingSignal(Region region, Signal signal) {
	}

	default void usingTransition(Region region, Transition transition) {
	}

	default void enteringCompositeState(Region region, CompositeState vertex) {
	}

	default void leavingCompositeState(Region region, CompositeState vertex) {
	}
	
	default void executionTerminated() {
	}
	
}
