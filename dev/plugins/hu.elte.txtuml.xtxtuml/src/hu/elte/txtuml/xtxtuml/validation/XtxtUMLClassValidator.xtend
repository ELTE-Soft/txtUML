package hu.elte.txtuml.xtxtuml.validation;

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.common.XtxtUMLClassDataTypeSignalHelper
import hu.elte.txtuml.xtxtuml.common.XtxtUMLExternalityHelper
import hu.elte.txtuml.xtxtuml.common.XtxtUMLUtils
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClassMember
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClassOrDataTypeOrSignal
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor
import hu.elte.txtuml.xtxtuml.xtxtUML.TUEntryOrExitActivity
import hu.elte.txtuml.xtxtuml.xtxtUML.TUState
import hu.elte.txtuml.xtxtuml.xtxtUML.TUStateMember
import hu.elte.txtuml.xtxtuml.xtxtUML.TUStateType
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransition
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionGuard
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionMember
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionPort
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionTrigger
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionVertex
import org.eclipse.emf.common.util.EList
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EStructuralFeature
import org.eclipse.xtext.EcoreUtil2
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.validation.Check

import static hu.elte.txtuml.xtxtuml.validation.XtxtUMLIssueCodes.*
import static hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage.Literals.*

class XtxtUMLClassValidator extends XtxtUMLFileValidator {

	@Inject extension IQualifiedNameProvider;
	@Inject extension XtxtUMLExternalityHelper;
	@Inject extension XtxtUMLUtils;
	@Inject extension XtxtUMLClassDataTypeSignalHelper

	@Check
	def checkNoCycleInClassDataTypeSignalHierarchy(TUClassOrDataTypeOrSignal general ) {
		if (general.travelHierarchy[false] == null) {
			error("Cycle in hierarchy of signal " + general.name, general, TU_SIGNAL__SUPER_SIGNAL,
				SIGNAL_HIERARCHY_CYCLE);
		}
	}
	
	@Check
	def checkConstructorName(TUConstructor ctor) {
		if (ctor.eContainer instanceof TUClass) {
			val name = ctor.name;
			val enclosingClassName = (ctor.eContainer as TUClass).name;
			if (name != enclosingClassName) {
				error('''Constructor «name»(«ctor.parameters.typeNames.join(", ")») in class «enclosingClassName» must be named as its enclosing class''',
					ctor, TU_CONSTRUCTOR__NAME, INVALID_CONSTRUCTOR_NAME);
			}		
		}
	}

	@Check
	def checkInitializerIsUsedOnlyOnExternalAttribute(TUAttribute attr) {
		if (attr.initExpression != null && !attr.isExternal) {
			error("Attribute " + attr.classQualifiedName + " cannot have an initializer expression" +
				" – initializer expressions can be defined only for external attributes", attr,
				TU_ATTRIBUTE__INIT_EXPRESSION, INITIALIZER_ON_NON_EXTERNAL_ATTRIBUTE);
		}
	}

	@Check
	def checkInitialStateIsDefinedInClass(TUClass clazz) {
		if (clazz.members.isInitialStateMissing) {
			missingInitialState(clazz, "class " + clazz.name, TU_MODEL_ELEMENT__NAME)
		}
	}

	@Check
	def checkInitialStateIsDefinedInCompositeState(TUState state) {
		if (state.type == TUStateType.COMPOSITE && state.members.isInitialStateMissing) {
			missingInitialState(state, "composite state " + state.classQualifiedName, TU_STATE__NAME)
		}
	}

	@Check
	def checkPseudostateIsLeavable(TUState state) {
		if (state.isPseudostate && !state.membersOfEnclosingElement.exists [
			it instanceof TUTransition &&
				(it as TUTransition).sourceState?.fullyQualifiedName == state.fullyQualifiedName
		]) {
			error("There are no outgoing transitions from pseudostate " + state.classQualifiedName +
				" – state machines cannot stop in pseudostates", state, TU_STATE__NAME, NOT_LEAVABLE_PSEUDOSTATE);
		}
	}

	@Check
	def checkStateIsReachable(TUState state) {
		if (!state.membersOfEnclosingElement.isInitialStateMissing &&
			!state.isReachableFromInitialState(newHashSet, false)) {
			warning("State " + state.classQualifiedName + " is unreachable", state, TU_STATE__NAME, UNREACHABLE_STATE);
		}
	}

	@Check
	def checkStateOrTransitionIsDefinedInClassOrCompositeState(TUStateMember stateMember) {
		var isStateOrTransition = false;
		val nameAndMarkerTarget = switch (stateMember) {
			TUState: {
				isStateOrTransition = true;
				"State " -> TU_STATE__NAME
			}
			TUTransition: {
				isStateOrTransition = true;
				"Transition " -> TU_TRANSITION__NAME
			}
		}

		if (isStateOrTransition && stateMember.eContainer instanceof TUState &&
			(stateMember.eContainer as TUState).type != TUStateType.COMPOSITE) {
			error(nameAndMarkerTarget.key + (stateMember as TUClassMember).classQualifiedName +
				" can be defined only in a class or a composite state", stateMember, nameAndMarkerTarget.value,
				STATE_OR_TRANSITION_IN_NOT_COMPOSITE_STATE);
		}
	}

	@Check
	def checkNoActivityInPseudostate(TUEntryOrExitActivity activity) {
		if (activity.eContainer.isPseudostate) {
			error("Activities must not be present in pseudostate " +
				(activity.eContainer as TUState).classQualifiedName, activity, activity.markerTargetForStateActivity,
				ACTIVITY_IN_PSEUDOSTATE);
		}
	}

	@Check
	def checkMandatoryTransitionMembers(TUTransition transition) {
		var hasSource = false;
		var hasTarget = false;
		var hasTrigger = false;

		for (member : transition.members) {
			switch (member) {
				TUTransitionVertex:
					if (member.from) {
						hasSource = true;
						if (member.vertex.isPseudostate) {
							hasTrigger = true;
						}
					} else {
						hasTarget = true;
					}
				TUTransitionTrigger:
					hasTrigger = true
			}
		}

		if (!hasSource || !hasTarget || !hasTrigger) {
			error("Missing mandatory member ('from', 'to' or 'trigger') in transition " + transition.classQualifiedName,
				transition, TU_TRANSITION__NAME, MISSING_MANDATORY_TRANSITION_MEMBER);
		}
	}

	@Check
	def checkTransitionTargetIsNotInitialState(TUTransitionVertex transitionVertex) {
		if (!transitionVertex.from && transitionVertex.vertex?.type == TUStateType.INITIAL) {
			error("Initial state cannot be the target of transition " +
				(transitionVertex.eContainer as TUTransition).classQualifiedName, transitionVertex,
				TU_TRANSITION_VERTEX__VERTEX, TARGET_IS_INITIAL_STATE);
		}
	}

	@Check
	def checkGuardIsNotForInitialTransition(TUTransitionGuard transitionGuard) {
		val enclosingTransition = transitionGuard.eContainer as TUTransition;
		if (enclosingTransition.sourceState?.type == TUStateType.INITIAL) {
			error("Guards must not be present in initial transition " + enclosingTransition.classQualifiedName,
				transitionGuard, TU_TRANSITION_GUARD__GUARD, INVALID_TRANSITION_MEMBER);
		}
	}

	@Check
	def checkMemberOfTransitionFromPseudostate(TUTransitionMember transitionMember) {
		val enclosingTransition = transitionMember.eContainer as TUTransition;
		val sourceState = enclosingTransition.sourceState;

		if (sourceState != null && sourceState.isPseudostate &&
			(transitionMember instanceof TUTransitionTrigger || transitionMember instanceof TUTransitionPort)) {
			error(
				"Triggers and port restrictions must not be present in transition " +
					enclosingTransition.classQualifiedName + ", as its source is a pseudostate", transitionMember,
				transitionMember.markerTargetForTransitionMember, INVALID_TRANSITION_MEMBER);
		}
	}

	@Check
	def checkElseGuard(TUTransitionGuard guard) {
		if (guard.^else && (guard.eContainer as TUTransition).sourceState?.type != TUStateType.CHOICE) {
			error("'else' condition can be used only if the source of the transition is a choice pseudostate", guard,
				TU_TRANSITION_GUARD__ELSE, INVALID_ELSE_GUARD)
		}
	}

	@Check
	def checkOwnerOfTriggerPort(TUTransitionPort triggerPort) {
		val triggerEnclosingClass = EcoreUtil2.getContainerOfType(triggerPort, TUClass) as TUClass;
		if (!triggerEnclosingClass.ownsPort(triggerPort.port)) {
			error(triggerPort.port.name + " cannot be resolved as a port of class " + triggerEnclosingClass.name,
				triggerPort, TU_TRANSITION_PORT__PORT, NOT_OWNED_TRIGGER_PORT);
		}
	}

	@Check
	def checkTriggerPortIsBehavior(TUTransitionPort triggerPort) {
		if (triggerPort.port != null && !triggerPort.port.behavior) {
			error("Port " + triggerPort.port.name + " in class " + (triggerPort.port.eContainer as TUClass).name +
				" is not a behavior port", triggerPort, TU_TRANSITION_PORT__PORT, NOT_BEHAVIOR_TRIGGER_PORT)
		}
	}

	@Check
	def checkTransitionVertexLevel(TUTransitionVertex transitionVertex) {
		val enclosingTransition = transitionVertex.eContainer as TUTransition;
		if (transitionVertex.vertex != null && transitionVertex.vertex.eContainer.fullyQualifiedName !=
			enclosingTransition.eContainer.fullyQualifiedName) {
			error(
				"Invalid vertex " + transitionVertex.vertex.classQualifiedName + " in transition " +
					enclosingTransition.classQualifiedName + " – transitions must not cross state machine levels",
				transitionVertex, TU_TRANSITION_VERTEX__VERTEX, VERTEX_LEVEL_MISMATCH);
		}
	}

	/**
	 * An initial state is missing from a state machine iff
	 * it has at least one state, but none of them is initial.
	 */
	def protected isInitialStateMissing(EList<? extends EObject> members) {
		if (members == null) {
			return false;
		}

		var isOtherStateDefined = false;
		for (member : members) {
			if (member instanceof TUState) {
				if (member.type == TUStateType.INITIAL) {
					return false;
				} else {
					isOtherStateDefined = true;
				}
			}
		}

		return isOtherStateDefined;
	}

	def protected missingInitialState(EObject element, String name, EStructuralFeature markerTarget) {
		warning("Missing initial pseudostate in " + name +
			", therefore its other states and transitions are unreachable", element, markerTarget,
			MISSING_INITIAL_STATE);
	}

}
