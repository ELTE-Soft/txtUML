package hu.elte.txtuml.api.model.execution;

import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.StateMachine.Transition;
import hu.elte.txtuml.api.model.StateMachine.Vertex;

/**
 * Listener for runtime errors of the model execution.
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 */
public interface ErrorListener {

	default void lowerBoundOfMultiplicityOffended(ModelClass obj, Class<? extends AssociationEnd<?, ?>> assocEnd) {
	}

	default void upperBoundOfMultiplicityOffended(ModelClass obj, Class<? extends AssociationEnd<?, ?>> assocEnd) {
	}

	default void linkingDeletedObject(ModelClass obj) {
	}

	default void unlinkingDeletedObject(ModelClass obj) {
	}

	default void startingDeletedObject(ModelClass obj) {
	}

	default void objectCannotBeDeleted(ModelClass obj) {
	}

	default void guardsOfTransitionsAreOverlapping(Transition transition1, Transition transition, Vertex vertex) {
	}

	default void moreThanOneElseTransitionsFromChoice(Vertex choice) {
	}

	default void noTransitionFromChoice(Vertex choice) {
	}

	default void elseGuardFromNonChoiceVertex(Transition transition) {
	}

	default void multipleContainerForAnObject(ModelClass obj, Class<? extends AssociationEnd<?, ?>> assocEnd) {
	}

}
