package hu.elte.txtuml.api.model.execution.log;

import java.text.MessageFormat;

import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.StateMachine.Transition;
import hu.elte.txtuml.api.model.StateMachine.Vertex;
import hu.elte.txtuml.api.model.execution.ErrorListener;

/**
 * Writes into the {@link hu.elte.txtuml.utils.Logger#executor} log about every
 * error event of a model executor.
 */
public class ErrorLogger extends LoggerBase implements ErrorListener {

	public ErrorLogger(String nameOfExecutor) {
		super(nameOfExecutor);
	}

	@Override
	public void guardsOfTransitionsAreOverlapping(Transition transition1, Transition transition2, Vertex vertex) {
		error("Guards of " + transition1 + " and " + transition2 + " from vertex " + vertex + " are overlapping.");
	}

	@Override
	public void moreThanOneElseTransitionsFromChoice(Vertex choice) {
		error("There are more than one transitions from " + choice + " with an 'else' condition.");
	}

	@Override
	public void noTransitionFromChoice(Vertex choice) {
		error("There was no transition from " + choice + " which could be used.");
	}

	@Override
	public void linkingDeletedObject(ModelClass obj) {
		error("Trying to link deleted model object " + obj + ".");
	}

	@Override
	public void unlinkingDeletedObject(ModelClass obj) {
		error("Trying to unlink deleted model object " + obj + ".");
	}

	@Override
	public void upperBoundOfMultiplicityOffended(ModelClass obj, Class<? extends AssociationEnd<?, ?>> assocEnd) {
		error("Upper bound of the multiplicity of " + assocEnd.getName() + " has been offended at "
				+ obj.runtimeInfo().getName() + ".");
	}

	@Override
	public void lowerBoundOfMultiplicityOffended(ModelClass obj, Class<? extends AssociationEnd<?, ?>> assocEnd) {
		error("Lower bound of the multiplicity of " + assocEnd.getName() + " has been offended at "
				+ obj.runtimeInfo().getName() + ".");
	}

	@Override
	public void objectCannotBeDeleted(ModelClass obj) {
		error("Model object " + obj + " cannot be deleted because of existing associations.");
	}

	@Override
	public void startingDeletedObject(ModelClass obj) {
		error("Trying to start deleted model object " + obj + ".");
	}

	@Override
	public void elseGuardFromNonChoiceVertex(Transition transition) {
		error("The transition " + transition + " has an 'else' guard but its source is not a choice pseudostate.");
	}

	@Override
	public void multipleContainerForAnObject(ModelClass obj, Class<? extends AssociationEnd<?, ?>> assocEnd) {
		error(MessageFormat.format(
				"Model object {0} cannot be put into container {1}, because it is already inside another container.",
				obj, assocEnd.getName()));
	}

}
