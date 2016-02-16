package hu.elte.txtuml.api.model.report;

import hu.elte.txtuml.api.model.Region;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.StateMachine.Transition;
import hu.elte.txtuml.api.model.StateMachine.Vertex;

/**
 * A listener interface that listens to valid events of the model execution.
 */
public interface ModelExecutionEventsListener {

	default void processingSignal(Region region, Signal signal) {
	}

	default void usingTransition(Region region, Transition transition) {
	}

	default void enteringVertex(Region region, Vertex vertex) {
	}

	default void leavingVertex(Region region, Vertex vertex) {
	}

	default void executionTerminated() {
	}

}
