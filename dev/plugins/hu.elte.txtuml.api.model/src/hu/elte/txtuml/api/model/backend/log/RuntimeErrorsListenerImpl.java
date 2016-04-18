package hu.elte.txtuml.api.model.backend.log;

import java.text.MessageFormat;

import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.StateMachine.Transition;
import hu.elte.txtuml.api.model.StateMachine.Vertex;
import hu.elte.txtuml.api.model.report.RuntimeErrorsListener;

final class RuntimeErrorsListenerImpl extends BaseListenerImpl implements RuntimeErrorsListener {

	RuntimeErrorsListenerImpl(ExecutorLog owner) {
		super(owner);
	}

	@Override
	public void badModel() {
		err("Error: model execution failed due to an error in the model.");
	}

	@Override
	public void modelObjectCreationFailed(Class<? extends ModelClass> classType, Object[] parameters) {
		StringBuilder builder = new StringBuilder();
		builder.append("Error: creating a model object of the type ");
		builder.append(classType.getSimpleName());
		builder.append(" has failed with the following parameters: ");
		if (parameters.length == 0) {
			builder.append("<none>");
		} else {
			for (Object param : parameters) {
				builder.append(param);
				if (param != null) {
					builder.append(" (" + param.getClass().getSimpleName() + ")");
				}
				builder.append(", ");
			}
			builder.delete(builder.length() - 2, builder.length() - 1);
		}
		err(builder.toString());
	}

	@Override
	public void changingLockedExecutionTimeMultiplier() {
		err("Error: model execution time multiplier might only be changed"
				+ "before any time-related events happen during model execution.");
	}

	@Override
	public void guardsOfTransitionsAreOverlapping(Transition transition1, Transition transition2, Vertex vertex) {
		err("Error: guards of " + transition1 + " and " + transition2 + " from vertex " + vertex + " are overlapping.");
	}

	@Override
	public void moreThanOneElseTransitionsFromChoice(Vertex choice) {
		err("Error: there are more than one transitions from " + choice + " with an 'else' condition.");
	}

	@Override
	public void missingInitialTransition(Vertex fromInitial) {
		err("Error: no initial transition is defined from " + fromInitial + ".");
	}

	@Override
	public void noTransitionFromChoice(Vertex choice) {
		err("Error: there was no transition from " + choice + " which could be used.");
	}

	@Override
	public void linkingDeletedObject(ModelClass obj) {
		err("Error: trying to link deleted model object " + obj + ".");
	}

	@Override
	public void unlinkingDeletedObject(ModelClass obj) {
		err("Error: trying to unlink deleted model object " + obj + ".");
	}

	@Override
	public void upperBoundOfMultiplicityOffended(ModelClass obj, Class<? extends AssociationEnd<?, ?>> assocEnd) {
		err("Error: upper bound of the multiplicity of " + assocEnd.toString() + " has been offended at " + obj + ".");
	}

	@Override
	public void lowerBoundOfMultiplicityOffended(ModelClass obj, Class<? extends AssociationEnd<?, ?>> assocEnd) {
		err("Error: lower bound of the multiplicity of " + assocEnd.getName() + " has been offended at " + obj + ".");
	}

	@Override
	public void objectCannotBeDeleted(ModelClass obj) {
		err("Error: model object " + obj + " cannot be deleted because of existing associations.");
	}

	@Override
	public void startingDeletedObject(ModelClass obj) {
		err("Error: trying to start deleted model object " + obj + ".");
	}

	@Override
	public void elseGuardFromNonChoiceVertex(Transition transition) {
		err("Error: the transition " + transition.toString()
				+ " has an 'else' guard but its source is not a choice pseudostate.");
	}

	@Override
	public void multipleContainerForAnObject(ModelClass leftObj, Class<? extends AssociationEnd<?, ?>> rightEnd) {
		err(MessageFormat.format(
				"Error: Model object {0} cannot be put into container {1}, because it is already inside another container.",
				leftObj, rightEnd.getName()));
	}
}
