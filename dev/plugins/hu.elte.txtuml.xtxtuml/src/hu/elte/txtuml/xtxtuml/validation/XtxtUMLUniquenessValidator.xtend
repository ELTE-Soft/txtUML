package hu.elte.txtuml.xtxtuml.validation;

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClassMember
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnector
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnectorEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor
import hu.elte.txtuml.xtxtuml.xtxtUML.TUEntryOrExitActivity
import hu.elte.txtuml.xtxtuml.xtxtUML.TUFile
import hu.elte.txtuml.xtxtuml.xtxtUML.TUModelElement
import hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUPort
import hu.elte.txtuml.xtxtuml.xtxtUML.TUPortMember
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
	def checkConstructorIsUnique(TUConstructor ctor) {
		val enclosingClass = (ctor.eContainer as TUClass);
		if (enclosingClass.members.exists [
			it instanceof TUConstructor && {
				val otherCtor = it as TUConstructor;
				otherCtor.name == ctor.name && otherCtor.parameters.typeNames == ctor.parameters.typeNames
			} && it != ctor // direct comparison is safe here
		]) {
			error('''Duplicate constructor «ctor.name»(«ctor.parameters.typeNames.join(", ")») in class «enclosingClass.name»''',
				ctor, TU_CONSTRUCTOR__NAME, NOT_UNIQUE_CONSTRUCTOR);
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
						siblingOperationOrSelf.parameters.typeNames == operation.parameters.typeNames
				} && it != operation // direct comparison is safe here
		]) {
			error('''Duplicate operation «operation.name»(«operation.parameters.typeNames.join(", ")») in class «containingClass.name»''',
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
		if (state.membersOfEnclosingElement.exists [
			it instanceof TUState && (it as TUState).name == state.name && it != state || // direct comparison is safe here
			it instanceof TUTransition && (it as TUTransition).name == state.name ||
				it instanceof TUPort && (it as TUPort).name == state.name
		]) {
			error("State " + state.classQualifiedName +
				" must have a unique name among states, transitions and ports of the enclosing element", state,
				TU_STATE__NAME, NOT_UNIQUE_NAME);
		}
	}

	@Check
	def checkStateActivityIsUnique(TUEntryOrExitActivity stateActivity) {
		if ((stateActivity.eContainer as TUState).members.exists [
			it instanceof TUEntryOrExitActivity && (it as TUEntryOrExitActivity).entry == stateActivity.entry &&
				it != stateActivity // direct comparison is safe here
		]) {
			error(
				"Duplicate state activity in state " + (stateActivity.eContainer as TUState).classQualifiedName,
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
				it instanceof TUTransition && (it as TUTransition).sourceState == sourceState && it != transition
			]) {
				error("Duplicate initial transition " + transition.classQualifiedName +
					" – only one per initial state is allowed", transition, TU_TRANSITION__NAME,
					NOT_UNIQUE_INITIAL_TRANSITION);
			}
		}
	}

	@Check
	def checkTransitionNameIsUnique(TUTransition transition) {
		if (transition.membersOfEnclosingElement.exists [
			it instanceof TUTransition && (it as TUTransition).name == transition.name && it != transition || // direct comparison is safe here
			it instanceof TUState && (it as TUState).name == transition.name ||
				it instanceof TUPort && (it as TUPort).name == transition.name
		]) {
			error("Transition " + transition.classQualifiedName +
				" must have a unique name among states, transitions and ports of the enclosing element", transition,
				TU_TRANSITION__NAME, NOT_UNIQUE_NAME);
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
				" – names and roles must be unique among ends of a connector", connectorEnd, TU_CONNECTOR_END__NAME,
				NOT_UNIQUE_CONNECTOR_END);
		}
	}

	def protected typeNames(EList<JvmFormalParameter> parameters) {
		parameters.map[parameterType.type.fullyQualifiedName]
	}

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

}
