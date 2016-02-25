package hu.elte.txtuml.api.model.execution;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.StateMachine.Transition;
import hu.elte.txtuml.api.model.StateMachine.Vertex;

/**
 * Listener for valid events of the model execution.
 */
public interface TraceListener {

	default void executionStarted() {
	}

	default void processingSignal(ModelClass object, Signal signal) {
	}

	default void usingTransition(ModelClass object, Transition transition) {
	}

	default void enteringVertex(ModelClass object, Vertex vertex) {
	}

	default void leavingVertex(ModelClass object, Vertex vertex) {
	}

	default void executionTerminated() {
	}

}
