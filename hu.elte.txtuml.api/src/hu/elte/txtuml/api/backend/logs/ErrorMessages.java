package hu.elte.txtuml.api.backend.logs;

import hu.elte.txtuml.api.ModelClass;
import hu.elte.txtuml.api.StateMachine.Transition;
import hu.elte.txtuml.api.StateMachine.Vertex;

/**
 * This interface contains static methods to create specified error messages.
 *
 * @author Gabor Ferenc Kovacs
 *
 */
@SuppressWarnings("javadoc")
public interface ErrorMessages {

	static String getUpperBoundOfMultiplicityOffendedMessage() {
		return "Error: upper bound of an association end's multiplicity has been offended.";
	}

	static String getLinkingDeletedObjectMessage(ModelClass obj) {
		return "Error: trying to link deleted model object " + obj.toString()
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

}
