package hu.elte.txtuml.xtxtuml.validation;

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnector
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnectorEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUFile
import hu.elte.txtuml.xtxtuml.xtxtUML.TUModelElement
import hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUPort
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignal
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUState
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransition
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.validation.Check

import static hu.elte.txtuml.xtxtuml.validation.XtxtUMLIssueCodes.*
import static hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage.Literals.*

class XtxtUMLUniquenessValidator extends AbstractXtxtUMLValidator {

	@Inject extension IQualifiedNameProvider;

	@Check
	def checkModelElementNameIsUniqueExternal(TUModelElement modelElement) {
		try {
			Class.forName(modelElement.fullyQualifiedName.toString, false, getClass.getClassLoader);

			// class with the same qualified name is found
			error("Duplicate model element " + modelElement.name, modelElement, TU_MODEL_ELEMENT__NAME,
				NOT_UNIQUE_NAME);
		} catch (ClassNotFoundException ex) {
			// no problem
		}
	}

	@Check
	def checkModelElementNameIsUniqueInternal(TUModelElement modelElement) {
		val siblingsAndSelf = (modelElement.eContainer as TUFile).elements;
		if (siblingsAndSelf.exists [
			name == modelElement.name && it != modelElement // direct comparison is safe here
		]) {
			error("Duplicate model element " + modelElement.name, modelElement, TU_MODEL_ELEMENT__NAME,
				NOT_UNIQUE_NAME);
		}
	}

	/*
	 * TODO remove when general TUAttribute is introduced
	 * TODO override local variable shadowing check defined in AbstractTypeComputationState
	 */
	@Check
	def checkSignalAttributeNameIsUnique(TUSignalAttribute attribute) {
		val containingSignal = attribute.eContainer as TUSignal;
		if (containingSignal.attributes.exists [
			name == attribute.name && it != attribute // direct comparison is safe here
		]) {
			error("Duplicate attribute " + attribute.name + " in signal " + containingSignal.name, attribute,
				TU_SIGNAL_ATTRIBUTE__NAME, NOT_UNIQUE_NAME);
		}
	}

	@Check
	def checkAttributeNameIsUnique(TUAttribute attribute) {
		val containingClass = attribute.eContainer as TUClass;
		if (containingClass.members.exists [
			it instanceof TUAttribute && (it as TUAttribute).name == attribute.name && it != attribute // direct comparison is safe here
		]) {
			error("Duplicate attribute " + attribute.name + " in class " + containingClass.name, attribute,
				TU_ATTRIBUTE__NAME, NOT_UNIQUE_NAME);
		}
	}

	@Check
	def checkOperationIsUnique(TUOperation operation) {
		val containingClass = (operation.eContainer as TUClass);
		if (containingClass.members.exists [
			it instanceof TUOperation &&
				{
					val siblingOperationOrSelf = it as TUOperation;
					siblingOperationOrSelf.name == operation.name &&
						siblingOperationOrSelf.parameterTypeList == operation.parameterTypeList
				} && it != operation // direct comparison is safe here
		]) {
			error('''Duplicate operation «operation.name»(«operation.parameterTypeList.join(", ")») in class «containingClass.name»''',
				operation, TU_OPERATION__NAME, NOT_UNIQUE_OPERATION);
		}
	}

	@Check
	def checkStateNameIsUnique(TUState state) {
		val container = state.eContainer;
		var inClass = false;
		val siblingsAndSelf = if (container instanceof TUClass) {
				inClass = true;
				(container as TUClass).members
			} else {
				(container as TUState).members
			}

		if (siblingsAndSelf.exists [
			it instanceof TUState && (it as TUState).name == state.name && it != state || // direct comparison is safe here
			it instanceof TUTransition && (it as TUTransition).name == state.name ||
				it instanceof TUPort && (it as TUPort).name == state.name
		]) {
			error("State " + state.name + " in " + (if (inClass)
				"class " + (container as TUClass).name
			else
				"state " + (container as TUState).name) +
				" must have a unique name among states, transitions and ports of the enclosing element", state,
				TU_STATE__NAME, NOT_UNIQUE_NAME);
		}
	}

	@Check
	def checkTransitionNameIsUnique(TUTransition transition) {
		val container = transition.eContainer;
		var inClass = false;
		val siblingsAndSelf = if (container instanceof TUClass) {
				inClass = true;
				(container as TUClass).members
			} else {
				(container as TUState).members
			}

		if (siblingsAndSelf.exists [
			it instanceof TUTransition && (it as TUTransition).name == transition.name && it != transition || // direct comparison is safe here
			it instanceof TUState && (it as TUState).name == transition.name ||
				it instanceof TUPort && (it as TUPort).name == transition.name
		]) {
			error("Transition " + transition.name + " in " + (if (inClass)
				"class " + (container as TUClass).name
			else
				"state " + (container as TUState).name) +
				" must have a unique name among states, transitions and ports of the enclosing element", transition,
				TU_TRANSITION__NAME, NOT_UNIQUE_NAME);
		}

	}

	@Check
	def checkPortNameIsUnique(TUPort port) {
		val containingClass = port.eContainer as TUClass;
		if (containingClass.members.exists [
			it instanceof TUPort && (it as TUPort).name == port.name && it != port // direct comparison is safe here
			|| it instanceof TUState && (it as TUState).name == port.name ||
				it instanceof TUTransition && (it as TUTransition).name == port.name
		]) {
			error("Port " + port.name + " in class " + containingClass.name +
				" must have a unique name among states, transitions and ports of the enclosing element", port,
				TU_CLASS_PROPERTY__NAME, NOT_UNIQUE_NAME);
		}
	}

	@Check
	def checkAssociationEndNamesAreUnique(TUAssociationEnd associationEnd) {
		val association = associationEnd.eContainer as TUAssociation;
		if (1 < association.ends.filter[name == associationEnd.name].length) {
			error("Association end " + associationEnd.name + " in association " + association.name +
				" must have a unique name", associationEnd, TU_CLASS_PROPERTY__NAME, NOT_UNIQUE_NAME);
		}
	}

	@Check
	def checkConnectorEndIsUnique(TUConnectorEnd connectorEnd) {
		val container = connectorEnd.eContainer as TUConnector;
		if (container.ends.exists [
			(it.name == connectorEnd.name || it.role.fullyQualifiedName == connectorEnd.role.fullyQualifiedName) &&
				it != connectorEnd // direct comparison is safe here
		]) {
			error("Duplicate connector end " + connectorEnd.name + " in connector " + container.name +
				". Names and roles must be unique among ends of a connector.", connectorEnd, TU_CONNECTOR_END__NAME,
				NOT_UNIQUE_CONNECTOR_END);
		}
	}

	def protected parameterTypeList(TUOperation op) {
		op.parameters.map[parameterType.type.fullyQualifiedName]
	}

}
