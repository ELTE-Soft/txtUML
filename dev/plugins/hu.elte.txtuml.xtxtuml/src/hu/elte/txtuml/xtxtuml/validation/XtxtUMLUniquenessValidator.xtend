package hu.elte.txtuml.xtxtuml.validation;

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.common.XtxtUMLClassDataTypeSignalHelper
import hu.elte.txtuml.xtxtuml.common.XtxtUMLUtils
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClassMember
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnector
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnectorEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor
import hu.elte.txtuml.xtxtuml.xtxtUML.TUEntryOrExitActivity
import hu.elte.txtuml.xtxtuml.xtxtUML.TUEnumeration
import hu.elte.txtuml.xtxtuml.xtxtUML.TUEnumerationLiteral
import hu.elte.txtuml.xtxtuml.xtxtUML.TUExecution
import hu.elte.txtuml.xtxtuml.xtxtUML.TUExecutionAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUExecutionBlock
import hu.elte.txtuml.xtxtuml.xtxtUML.TUFile
import hu.elte.txtuml.xtxtuml.xtxtUML.TUInterface
import hu.elte.txtuml.xtxtuml.xtxtUML.TUModelElement
import hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUPort
import hu.elte.txtuml.xtxtuml.xtxtUML.TUPortMember
import hu.elte.txtuml.xtxtuml.xtxtUML.TUReception
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignal
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUState
import hu.elte.txtuml.xtxtuml.xtxtUML.TUStateMember
import hu.elte.txtuml.xtxtuml.xtxtUML.TUStateType
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransition
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionEffect
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionGuard
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionMember
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionPort
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionTrigger
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionVertex
import org.eclipse.emf.common.util.EList
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.EcoreUtil2
import org.eclipse.xtext.common.types.JvmFormalParameter
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.validation.Check

import static hu.elte.txtuml.xtxtuml.validation.XtxtUMLIssueCodes.*
import static hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage.Literals.*

class XtxtUMLUniquenessValidator extends XtxtUMLNameValidator {

	@Inject extension IQualifiedNameProvider;
	@Inject extension XtxtUMLUtils;
	@Inject extension XtxtUMLClassDataTypeSignalHelper;

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
		val duplicateName = siblingsAndSelf.findFirst [
			name.toLowerCase == modelElement.name.toLowerCase && it != modelElement
		]?.name; // direct comparison is safe here

		if (duplicateName != null) {
			error("Duplicate model element " + modelElement.name +
				optionalCaseInsensitivityWarning(modelElement.name, duplicateName), modelElement, TU_MODEL_ELEMENT__NAME,
				NOT_UNIQUE_NAME);
		}
	}

	@Check
	def checkEnumerationLiteralNameIsUnique(TUEnumerationLiteral literal) {
		val containingEnumeration = literal.eContainer as TUEnumeration;
		if (containingEnumeration.literals.exists [
			name == literal.name && it != literal // direct comparison is safe here
		]) {
			error("Duplicate literal " + literal.name + " in enumeration " + containingEnumeration.name, literal,
				TU_ENUMERATION_LITERAL__NAME, NOT_UNIQUE_NAME);
		}
	}
	
	@Check
	def checkExecutionAttributeNameIsUnique(TUExecutionAttribute execAttr) {
		val containingClass = execAttr.eContainer as TUExecution;
		if (containingClass.elements.exists [
			it instanceof TUExecutionAttribute && (it as TUExecutionAttribute).name == execAttr.name
				&& it != execAttr // direct comparison is safe here
		]) {
			error("Duplicate attribute " + execAttr.name + " in execution " + containingClass.name, execAttr,
				TU_EXECUTION_ATTRIBUTE__NAME, NOT_UNIQUE_NAME);
		}
	}

	@Check
	def checkExecutionBlockIsUnique(TUExecutionBlock execBlock) {
		val containingClass = execBlock.eContainer as TUExecution;
		if (containingClass.elements.exists [
			it instanceof TUExecutionBlock && (it as TUExecutionBlock).type == execBlock.type
				&& it != execBlock // direct comparison is safe here
		]) {
			error("Duplicate execution block " + execBlock.type + " in execution " + containingClass.name, execBlock,
				TU_EXECUTION_BLOCK__TYPE, NOT_UNIQUE_NAME);
		}
	}

	@Check
	def checkReceptionIsUnique(TUReception reception) {
		val enclosingInterface = reception.eContainer as TUInterface;
		if (enclosingInterface.receptions.exists [
			signal.fullyQualifiedName == reception.signal.fullyQualifiedName && it != reception // direct comparison is safe here
		]) {
			error("Duplicate reception in interface " + enclosingInterface.name, reception, TU_RECEPTION__SIGNAL,
				NOT_UNIQUE_RECEPTION);
		}
	}

	/*
	 * TODO remove when general TUAttribute is introduced
	 * TODO override local variable shadowing check defined in AbstractTypeComputationState
	 */
	@Check
	def checkSignalAttributeIsUnique(TUSignalAttribute attribute) {
		val containingSignal = attribute.eContainer as TUSignal;
		if (containingSignal.travelTypeHierarchy [
			(it as TUSignal).attributes.findFirst [
				name == attribute.name && it != attribute // direct comparison is safe here
			] != null
		]) {
			error("Duplicate attribute " + attribute.name + " in signal " + containingSignal.name, attribute,
				TU_SIGNAL_ATTRIBUTE__NAME, NOT_UNIQUE_SIGNAL_ATTRIBUTE);
		}
	}

	@Check
	def checkAttributeNameIsUnique(TUAttribute attribute) {
		val containing = attribute.eContainer;
		if (containing.members.exists [
			it instanceof TUAttribute && (it as TUAttribute).name == attribute.name && it != attribute // direct comparison is safe here
		]) {
			error("Duplicate attribute " + attribute.name + " in "+ containing.typeString + " " + containing.name, attribute,
				TU_ATTRIBUTE__NAME, NOT_UNIQUE_NAME);
		}
	}
	
	@Check
	def checkConstructorIsUnique(TUConstructor ctor) {
		val enclosing = ctor.eContainer;
		if (enclosing.members.exists [
				it instanceof TUConstructor && {
					val otherCtor = it as TUConstructor;
					otherCtor.name == ctor.name && otherCtor.parameters.typeNames == ctor.parameters.typeNames
				} && it != ctor // direct comparison is safe here
			]) {
				error('''Duplicate constructor «ctor.name»(«ctor.parameters.typeNames.join(", ")») in «enclosing.typeString» «enclosing.name»''',
					ctor, TU_CONSTRUCTOR__NAME, NOT_UNIQUE_CONSTRUCTOR);
			}
	}

	@Check
	def checkOperationIsUnique(TUOperation operation) {
		val containing = operation.eContainer;
		if (containing.members.exists [
			it instanceof TUOperation &&
				{
					val siblingOperationOrSelf = it as TUOperation;
					siblingOperationOrSelf.name == operation.name &&
						siblingOperationOrSelf.parameters.typeNames == operation.parameters.typeNames
				} && it != operation // direct comparison is safe here
		]) {
			error('''Duplicate operation «operation.name»(«operation.parameters.typeNames.join(", ")») in «containing.typeString» «containing.name»''',
				operation, TU_OPERATION__NAME, NOT_UNIQUE_OPERATION);
		}
	}

	@Check
	def checkInitialStateIsUnique(TUState state) {
		if (state.type == TUStateType.INITIAL && state.membersOfEnclosingElement.exists [
			it instanceof TUState && (it as TUState).type == TUStateType.INITIAL && it != state // direct comparison is safe here
		]) {
			error("Duplicate initial pseudostate " + state.classQualifiedName +
				" – only one initial pseudostate per state machine level is allowed", state, TU_STATE__NAME,
				NOT_UNIQUE_INITIAL_STATE);
		}
	}

	@Check
	def checkStateNameIsUnique(TUState state) {
		val lowerCaseName = state.name.toLowerCase;
		val duplicate = state.membersOfEnclosingElement.findFirst [
			it instanceof TUState && (it as TUState).name.toLowerCase == lowerCaseName && it != state || // direct comparison is safe here
			it instanceof TUTransition && (it as TUTransition).name.toLowerCase == lowerCaseName ||
				it instanceof TUPort && (it as TUPort).name.toLowerCase == lowerCaseName
		];

		if (duplicate != null) {
			error(
				"State " + state.classQualifiedName +
					" must have a unique name among states, transitions and ports of the enclosing element" +
					optionalCaseInsensitivityWarning(state.name, duplicate.nameOfClassLikeMember), state, TU_STATE__NAME,
				NOT_UNIQUE_NAME);
		}
	}

	@Check
	def checkStateActivityIsUnique(TUEntryOrExitActivity stateActivity) {
		if ((stateActivity.eContainer as TUState).members.exists [
			it instanceof TUEntryOrExitActivity && (it as TUEntryOrExitActivity).entry == stateActivity.entry &&
				it != stateActivity // direct comparison is safe here
		]) {
			error(
				"Duplicate activity in state " + (stateActivity.eContainer as TUState).classQualifiedName,
				stateActivity,
				stateActivity.markerTargetForStateActivity,
				NOT_UNIQUE_STATE_ACTIVITY
			);
		}
	}

	@Check
	def checkInitialTransitionIsUnique(TUTransition transition) {
		val sourceState = transition.sourceState;
		if (sourceState?.type == TUStateType.INITIAL) {
			if (transition.membersOfEnclosingElement.exists [
				it instanceof TUTransition && (it as TUTransition).sourceState?.fullyQualifiedName == sourceState?.fullyQualifiedName &&
					it != transition // direct comparison is safe here
			]) {
				error("Duplicate initial transition " + transition.classQualifiedName +
					" – only one per initial state is allowed", transition, TU_TRANSITION__NAME,
					NOT_UNIQUE_INITIAL_TRANSITION);
			}
		}
	}

	@Check
	def checkTransitionNameIsUnique(TUTransition transition) {
		val lowerCaseName = transition.name.toLowerCase;
		val duplicate = transition.membersOfEnclosingElement.findFirst [
			it instanceof TUTransition && (it as TUTransition).name.toLowerCase == lowerCaseName &&
				it != transition || // direct comparison is safe here
			it instanceof TUState && (it as TUState).name.toLowerCase == lowerCaseName ||
				it instanceof TUPort && (it as TUPort).name.toLowerCase == lowerCaseName
		];

		if (duplicate != null) {
			error(
				"Transition " + transition.classQualifiedName +
					" must have a unique name among states, transitions and ports of the enclosing element" +
					optionalCaseInsensitivityWarning(transition.name, duplicate.nameOfClassLikeMember),
				transition, TU_TRANSITION__NAME, NOT_UNIQUE_NAME);
		}
	}

	@Check
	def checkTransitionMemberIsUnique(TUTransitionMember transitionMember) {
		val enclosingTransition = transitionMember.eContainer as TUTransition;
		if (enclosingTransition.members.exists [
			eClass == transitionMember.eClass && (if (it instanceof TUTransitionVertex)
				from == (transitionMember as TUTransitionVertex).from
			else
				true) && it != transitionMember // direct comparison is safe here
		]) {
			error("Duplicate member in transition " + enclosingTransition.classQualifiedName, transitionMember,
				transitionMember.markerTargetForTransitionMember, NOT_UNIQUE_TRANSITION_MEMBER);
		}
	}

	@Check
	def checkPortNameIsUnique(TUPort port) {
		val lowerCaseName = port.name.toLowerCase;
		val containingClass = port.eContainer as TUClass;
		val duplicate = containingClass.members.findFirst [
			it instanceof TUPort && (it as TUPort).name.toLowerCase == lowerCaseName && it != port // direct comparison is safe here
			|| it instanceof TUState && (it as TUState).name.toLowerCase == lowerCaseName ||
				it instanceof TUTransition && (it as TUTransition).name.toLowerCase == lowerCaseName
		];

		if (duplicate != null) {
			error(
				"Port " + port.name + " in class " + containingClass.name +
					" must have a unique name among states, transitions and ports of the enclosing element" +
					optionalCaseInsensitivityWarning(port.name, duplicate.nameOfClassLikeMember), port,
				TU_CONNECTIVE_END__NAME, NOT_UNIQUE_NAME);
		}
	}

	@Check
	def checkPortMemberIsUnique(TUPortMember portMember) {
		val enclosingPort = portMember.eContainer as TUPort;
		if (enclosingPort.members.exists [
			required == portMember.required && it != portMember // direct comparison is safe here
		]) {
			error("Duplicate interface type in port " + enclosingPort.name + " of class " +
				(enclosingPort.eContainer as TUClass).name, portMember,
				if(portMember.required) TU_PORT_MEMBER__REQUIRED else TU_PORT_MEMBER__PROVIDED, NOT_UNIQUE_PORT_MEMBER);
		}
	}

	@Check
	def checkAssociationEndNamesAreUnique(TUAssociationEnd associationEnd) {
		val association = associationEnd.eContainer as TUAssociation;
		val duplicateName = association.ends.findFirst [
			name.toLowerCase == associationEnd.name.toLowerCase && it != associationEnd // direct comparison is safe here
		]?.name;

		if (duplicateName != null) {
			error("Association end " + associationEnd.name + " in association " + association.name +
				" must have a unique name" + optionalCaseInsensitivityWarning(associationEnd.name, duplicateName),
				associationEnd, TU_CONNECTIVE_END__NAME, NOT_UNIQUE_NAME);
		}
	}

	@Check
	def checkConnectorEndNamesAreUnique(TUConnectorEnd connectorEnd) {
		val connector = connectorEnd.eContainer as TUConnector;
		val duplicateName = connector.ends.findFirst [
			name.toLowerCase == connectorEnd.name.toLowerCase && it != connectorEnd // direct comparison is safe here
		]?.name;

		if (duplicateName != null) {
			error("Connector end " + connectorEnd.name + " in connector " + connector.name +
				" must have a unique name" + optionalCaseInsensitivityWarning(connectorEnd.name, duplicateName),
				connectorEnd, TU_CONNECTIVE_END__NAME, NOT_UNIQUE_NAME);
		}
	}

	def protected typeNames(EList<JvmFormalParameter> parameters) {
		parameters.map[parameterType?.type?.fullyQualifiedName]
	}

	/**
	 * Returns the class qualified name of the given class member. That is,
	 * the returned string will be the fully qualified name of the given
	 * member, starting from the simple name of its enclosing class.
	 */
	def protected classQualifiedName(TUClassMember classMember) {
		val fqnOfClass = EcoreUtil2.getContainerOfType(classMember, TUClass)?.fullyQualifiedName;
		if (fqnOfClass != null) {
			val fqnOfMember = classMember.fullyQualifiedName;
			return fqnOfClass.lastSegment + fqnOfMember.toString.substring(fqnOfClass.toString.length);
		}
	}

	def protected isPseudostate(EObject object) {
		object instanceof TUState && {
			val state = object as TUState;
			state.type == TUStateType.INITIAL || state.type == TUStateType.CHOICE
		}
	}

	def protected sourceState(TUTransition it) {
		sourceOrTargetState(true)
	}

	def protected targetState(TUTransition it) {
		sourceOrTargetState(false)
	}

	def private sourceOrTargetState(TUTransition it, boolean isSource) {
		(members.findFirst [
			it instanceof TUTransitionVertex && (it as TUTransitionVertex).from == isSource
		] as TUTransitionVertex)?.vertex
	}

	def protected markerTargetForStateActivity(TUEntryOrExitActivity stateActivity) {
		if(stateActivity.entry) TU_ENTRY_OR_EXIT_ACTIVITY__ENTRY else TU_ENTRY_OR_EXIT_ACTIVITY__EXIT
	}

	def protected markerTargetForTransitionMember(TUTransitionMember transitionMember) {
		switch (transitionMember) {
			TUTransitionTrigger: TU_TRANSITION_TRIGGER__TRIGGER_KEYWORD
			TUTransitionVertex: if(transitionMember.from) TU_TRANSITION_VERTEX__FROM else TU_TRANSITION_VERTEX__TO
			TUTransitionEffect: TU_TRANSITION_EFFECT__EFFECT
			TUTransitionGuard: TU_TRANSITION_GUARD__GUARD
			TUTransitionPort: TU_TRANSITION_PORT__PORT_KEYWORD
		}
	}

	def protected membersOfEnclosingElement(TUStateMember stateMember) {
		switch (container : stateMember.eContainer) {
			TUClass: container.members
			TUState: container.members
		}
	}

	/**
	 * Should be called only when <code>name1.toLowerCase() == name2.toLowerCase()</code> holds.
	 */
	def protected optionalCaseInsensitivityWarning(String name1, String name2) {
		if (name1 != name2) {
			" – uniqueness validation in this case is case insensitive"
		} else {
			""
		}
	}

	/**
	 * <code>it</code> should be either a state, a transition or a port.
	 */
	def protected nameOfClassLikeMember(Object it) {
		switch (it) {
			TUState: name
			TUTransition: name
			TUPort: name
		}
	}

}
