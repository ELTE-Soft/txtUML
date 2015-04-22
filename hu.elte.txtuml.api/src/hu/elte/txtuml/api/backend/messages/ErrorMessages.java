package hu.elte.txtuml.api.backend.messages;

import hu.elte.txtuml.api.AssociationEnd;
import hu.elte.txtuml.api.ModelClass;
import hu.elte.txtuml.api.StateMachine.Transition;
import hu.elte.txtuml.api.StateMachine.Vertex;

/**
 * This interface contains static methods to create specified error messages.
 *
 * @author Gabor Ferenc Kovacs
 *
 */
public interface ErrorMessages {

	static String getLowerBoundOfMultiplicityOffendedMessage(ModelClass obj,
			Class<? extends AssociationEnd<?>> assocEnd) {
		return "Error: lower bound of the multiplicity of "
				+ assocEnd.getName() + " has been offended at "
				+ obj.toString() + ".";
	}

	static String getUpperBoundOfMultiplicityOffendedMessage(ModelClass obj,
			Class<? extends AssociationEnd<?>> assocEnd) {
		return "Error: upper bound of the multiplicity of "
				+ assocEnd.toString() + " has been offended at "
				+ obj.toString() + ".";
	}

	static String getLinkingDeletedObjectMessage(ModelClass obj) {
		return "Error: trying to link deleted model object " + obj.toString()
				+ ".";
	}

	static String getUnlinkingDeletedObjectMessage(ModelClass obj) {
		return "Error: trying to unlink deleted model object " + obj.toString()
				+ ".";
	}

	static String getStartingDeletedObjectMessage(ModelClass obj) {
		return "Error: trying to start deleted model object " + obj.toString()
				+ ".";
	}

	static String getObjectCannotBeDeletedMessage(ModelClass obj) {
		return "Error: model object " + obj.toString()
				+ " cannot be deleted because of existing associations.";
	}

	static String getGuardsOfTransitionsAreOverlappingMessage(
			Transition transition1, Transition transition2, Vertex vertex) {

		return "Error: guards of " + transition1.toString() + " and "
				+ transition2.toString() + " from vertex " + vertex.toString()
				+ " are overlapping.";
	}

	static String getMoreThanOneElseTransitionsFromChoiceMessage(Vertex choice) {
		return "Error: there are more than one transitions from "
				+ choice.toString() + " with an 'else' condition.";
	}

	static String getNoTransitionFromChoiceMessage(Vertex choice) {
		return "Error: there was no transition from " + choice.toString()
				+ " which could be used.";
	}

	static String getChangingLockedExecutionTimeMultiplierMessage() {
		return "Error: model execution time multiplier might only be changed"
				+ "before any time-related events happen during model execution.";
	}

	static String getBadModel() {
		return "Model execution failed due to an error in the model.";
	}

}
