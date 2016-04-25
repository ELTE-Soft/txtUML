package hu.elte.txtuml.api.model.report;

import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.StateMachine.Transition;
import hu.elte.txtuml.api.model.StateMachine.Vertex;

/**
 * Listener for runtime errors of the model execution.
 */
public interface RuntimeErrorsListener {

	default void lowerBoundOfMultiplicityOffended(ModelClass obj,
			Class<? extends AssociationEnd<?, ?>> assocEnd) {
	}

	default void upperBoundOfMultiplicityOffended(ModelClass obj,
			Class<? extends AssociationEnd<?, ?>> assocEnd) {
	}

	default void linkingDeletedObject(ModelClass obj) {
	}

	default void unlinkingDeletedObject(ModelClass obj) {
	}

	default void startingDeletedObject(ModelClass obj) {
	}

	default void objectCannotBeDeleted(ModelClass obj) {
	}

	default void guardsOfTransitionsAreOverlapping(Transition transition1,
			Transition transition2, Vertex vertex) {
	}

	default void moreThanOneElseTransitionsFromChoice(Vertex choice) {
	}

	default void noTransitionFromChoice(Vertex choice) {
	}

	default void missingInitialTransition(Vertex fromInitial) {
	}

	default void changingLockedExecutionTimeMultiplier() {
	}

	default void badModel() {
	}

	default void modelObjectCreationFailed(
			Class<? extends ModelClass> classType, Object[] parameters) {
	}

	default void elseGuardFromNonChoiceVertex(Transition transition) {
	}

	default void multipleContainerForAnObject(ModelClass leftObj, Class<? extends AssociationEnd<?, ?>> rightEnd) {
	}

}
