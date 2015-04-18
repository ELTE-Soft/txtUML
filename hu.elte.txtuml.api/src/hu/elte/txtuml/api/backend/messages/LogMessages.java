package hu.elte.txtuml.api.backend.messages;

import hu.elte.txtuml.api.Signal;
import hu.elte.txtuml.api.StateMachine;
import hu.elte.txtuml.api.StateMachine.Transition;
import hu.elte.txtuml.api.StateMachine.Vertex;

/**
 * This interface contains static methods to create specified log messages.
 *
 * @author Gabor Ferenc Kovacs
 *
 */
public interface LogMessages {

	static String getProcessingSignalMessage(StateMachine sm, Signal signal) {
		return sm.toString() + " processes " + signal.toString();
	}

	static String getUsingTransitionMessage(StateMachine sm,
			Transition transition) {
		return sm.toString() + " uses " + transition.toString();
	}

	static String getLeavingVertexMessage(StateMachine sm, Vertex vertex) {
		return sm.toString() + " leaves " + vertex.toString();
	}

	static String getEnteringVertexMessage(StateMachine sm, Vertex vertex) {
		return sm.toString() + " enters " + vertex.toString();
	}

}
