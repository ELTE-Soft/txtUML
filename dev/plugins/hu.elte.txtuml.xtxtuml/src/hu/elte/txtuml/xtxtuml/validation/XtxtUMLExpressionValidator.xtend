package hu.elte.txtuml.xtxtuml.validation;

import com.google.inject.Inject
import hu.elte.txtuml.api.model.ModelClass.Port
import hu.elte.txtuml.api.model.Signal
import hu.elte.txtuml.xtxtuml.common.XtxtUMLUtils
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClassPropertyAccessExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUDeleteObjectExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUEntryOrExitActivity
import hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUPort
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSendSignalExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignal
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAccessExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUState
import hu.elte.txtuml.xtxtuml.xtxtUML.TUStateType
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransition
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionGuard
import java.util.HashSet
import java.util.List
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.EcoreUtil2
import org.eclipse.xtext.common.types.JvmOperation
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.naming.QualifiedName
import org.eclipse.xtext.validation.Check
import org.eclipse.xtext.xbase.XAbstractFeatureCall
import org.eclipse.xtext.xbase.XBlockExpression
import org.eclipse.xtext.xbase.XExpression
import org.eclipse.xtext.xbase.XFeatureCall
import org.eclipse.xtext.xbase.XMemberFeatureCall
import org.eclipse.xtext.xbase.XbasePackage
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations
import org.eclipse.xtext.xbase.typesystem.util.ExtendedEarlyExitComputer

import static hu.elte.txtuml.xtxtuml.validation.XtxtUMLIssueCodes.*
import static hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage.Literals.*
import hu.elte.txtuml.xtxtuml.xtxtUML.TULinkExpression

class XtxtUMLExpressionValidator extends XtxtUMLTypeValidator {

	@Inject extension ExtendedEarlyExitComputer;
	@Inject extension IJvmModelAssociations;
	@Inject extension IQualifiedNameProvider;
	@Inject extension XtxtUMLUtils;

	@Check
	def checkMandatoryIntentionalReturn(TUOperation operation) {
		val returnTypeName = operation.prefix?.type?.type?.fullyQualifiedName;
		if (returnTypeName != null && returnTypeName.toString != "void" && !operation.body.definiteEarlyExit) {
			error('''Operation «operation.name» in class «(operation.eContainer as TUClass).name» must return a result of type «returnTypeName.lastSegment»''',
				operation, TU_OPERATION__NAME, MISSING_RETURN);
		}
	}

	@Check
	def checkNoExplicitExtensionCall(XFeatureCall featureCall) {
		doCheckNoExplicitExtensionCall(featureCall)
	}

	@Check
	def checkNoExplicitExtensionCall(XMemberFeatureCall featureCall) {
		doCheckNoExplicitExtensionCall(featureCall)
	}

	@Check
	def checkXtxtUMLExplicitOperationCall(XFeatureCall featureCall) {
		doCheckExplicitOperationCall(featureCall)
	}

	@Check
	def checkXtxtUMLExplicitOperationCall(XMemberFeatureCall featureCall) {
		doCheckExplicitOperationCall(featureCall)
	}

	@Check
	def checkSignalAccessExpression(TUSignalAccessExpression sigExpr) {
		var container = sigExpr.eContainer;
		while (container != null && !(container instanceof TUEntryOrExitActivity) &&
			!(container instanceof TUTransition)) {
			container = container.eContainer;
		}

		if (container instanceof TUEntryOrExitActivity) {
			if (container.entry) {
				container = container.eContainer;
			} else {
				// exit activities are invalid in a choice
				return;
			}
		}

		if (container == null || container.isReachableFromInitialState(newHashSet, true)) {
			error("'trigger' cannot be used here, as its container is directly reachable from the initial state",
				sigExpr, TU_SIGNAL_ACCESS_EXPRESSION__TRIGGER, INVALID_SIGNAL_ACCESS);
		}
	}

	@Check
	def checkSignalSentToPortIsRequired(TUSendSignalExpression sendExpr) {
		if (!sendExpr.target.isConformantWith(Port, false) || !sendExpr.signal.isConformantWith(Signal, false)) {
			return;
		}

		val sentSignalSourceElement = sendExpr.signal.actualType.type.primarySourceElement as TUSignal;

		val portSourceElement = sendExpr.target.actualType.type.primarySourceElement as TUPort;
		val requiredReceptionsOfPort = portSourceElement.members.findFirst[required]?.interface?.receptions;

		val List<QualifiedName> supers = newArrayList
		if (sentSignalSourceElement.travelSignalHierarchy [
			supers.add(fullyQualifiedName)
			false
		] == null) {
			return; // circle in hierarchy
		}

		if (requiredReceptionsOfPort?.findFirst [
			supers.contains(signal?.fullyQualifiedName)
		] == null) {
			error("Signal type " + sentSignalSourceElement.name + " is not required by port " + portSourceElement.name,
				sendExpr, TU_SEND_SIGNAL_EXPRESSION__SIGNAL, NOT_REQUIRED_SIGNAL);
		}
	}

	@Check
	def checkQueriedPortIsOwned(TUSendSignalExpression sendExpr) {
		if (!sendExpr.target.isConformantWith(Port, false)) {
			return;
		}

		val sendExprEnclosingClass = EcoreUtil2.getContainerOfType(sendExpr, TUClass) as TUClass;
		val portType = sendExpr.target.actualType.type;

		if (!sendExprEnclosingClass.ownsPort(portType)) {
			error(
				"Port " + portType.simpleName + " does not belong to class " + sendExprEnclosingClass.fullyQualifiedName.lastSegment +
					" – signals can be sent only to owned ports",
				sendExpr,
				TU_SEND_SIGNAL_EXPRESSION__TARGET,
				QUERIED_PORT_IS_NOT_OWNED
			);
		}
	}

	@Check
	def checkAccessedClassPropertyIsSpecified(TUClassPropertyAccessExpression propAccessExpr) {
		if (propAccessExpr.right == null) {
			error("The accessed class property cannot be null", propAccessExpr,
				TU_CLASS_PROPERTY_ACCESS_EXPRESSION__ARROW, MISSING_CLASS_PROPERTY);
		}
	}

	@Check
	def checkOwnerOfAccessedClassProperty(TUClassPropertyAccessExpression propAccessExpr) {
		if (propAccessExpr.right == null) {
			return;
		}

		val leftSourceElement = propAccessExpr.left.actualType.type.primarySourceElement;
		if (!(leftSourceElement instanceof TUClass)) {
			return; // typechecks will mark it
		}

		val sourceClass = leftSourceElement as TUClass;
		switch (prop : propAccessExpr.right) {
			TUAssociationEnd: {
				val enclosingAssociation = prop.eContainer as TUAssociation;
				val otherEndClassName = enclosingAssociation.ends.findFirst[name != prop.name]?.endClass
					?.fullyQualifiedName;
				
				if (sourceClass.travelClassHierarchy[fullyQualifiedName == otherEndClassName] == false) {
					error(
						"Association end " + enclosingAssociation.name + "." + prop.name +
							" is not accessible from class " + sourceClass?.name, propAccessExpr,
						TU_CLASS_PROPERTY_ACCESS_EXPRESSION__RIGHT, NOT_ACCESSIBLE_ASSOCIATION_END);
				} else if (prop.notNavigable) {
					error("Association end " + enclosingAssociation.name + "." + prop.name + " is not navigable",
						propAccessExpr, TU_CLASS_PROPERTY_ACCESS_EXPRESSION__RIGHT, NOT_NAVIGABLE_ASSOCIATION_END);
				}
			}
			TUPort: {
				if (!sourceClass.ownsPort(prop)) {
					error(prop.name + " cannot be resolved as a port of class " + sourceClass.name, propAccessExpr,
						TU_CLASS_PROPERTY_ACCESS_EXPRESSION__RIGHT, NOT_ACCESSIBLE_PORT);
				}
			}
		}
	}

	/*
	 * TODO modify ExtensionScopeHelper
	 */
	def private doCheckNoExplicitExtensionCall(XAbstractFeatureCall featureCall) {
		if (featureCall.isExtension) {
			val actualArgs = featureCall.actualArguments;
			error('''The operation «featureCall.feature?.simpleName»(«actualArgs.drop(1).join(", ")[actualType.simpleName.replace("$", ".")]») is undefined for the class «actualArgs.head.actualType.simpleName.replace("$", ".")»''',
				featureCall, XbasePackage.Literals.XABSTRACT_FEATURE_CALL__FEATURE, UNDEFINED_OPERATION);
		}
	}

	def private doCheckExplicitOperationCall(XAbstractFeatureCall featureCall) {
		val explicitOperationCall = if (featureCall instanceof XFeatureCall) {
				featureCall.isExplicitOperationCall
			} else if (featureCall instanceof XMemberFeatureCall) {
				featureCall.isExplicitOperationCall
			} else {
				true
			}

		if (featureCall.feature instanceof JvmOperation && !explicitOperationCall) {
			error("Empty parentheses are required for operations without parameters", featureCall,
				XbasePackage.Literals.XABSTRACT_FEATURE_CALL__FEATURE, MISSING_OPERATION_PARENTHESES);
		}
	}

	/**
	 * The purpose of this dispatch case is to enforce the common <code>EObject</code> supertype (which would
	 * otherwise be <code>TUClassOrStateMember</code>).
	 */
	def protected dispatch boolean isReachableFromInitialState(EObject object, HashSet<TUState> visitedStates,
		boolean throughPseudostatesOnly) {
		throw new IllegalArgumentException("Unhandled parameter type: EObject");
	}

	def protected dispatch boolean isReachableFromInitialState(TUTransition transition, HashSet<TUState> visitedStates,
		boolean throughPseudostatesOnly) {
		val from = transition.sourceState;
		return from != null && (!throughPseudostatesOnly || from.isPseudostate) &&
			isReachableFromInitialState(from, visitedStates, throughPseudostatesOnly);
	}

	/**
	 * @param throughPseudostatesOnly whether only pseudostates should be considered while
	 * checking if the given state is reachable from the initial state of the enclosing state machine
	 */
	def protected dispatch boolean isReachableFromInitialState(TUState state, HashSet<TUState> visitedStates,
		boolean throughPseudostatesOnly) {
		if (state.type == TUStateType.INITIAL) {
			return true;
		}

		if (!visitedStates.add(state)) {
			return false;
		}

		return state.membersOfEnclosingElement.exists [
			it instanceof TUTransition &&
				(it as TUTransition).targetState?.fullyQualifiedName == state?.fullyQualifiedName &&
				isReachableFromInitialState(it, visitedStates, throughPseudostatesOnly)
		];
	}

	/**
	 * Extends the default behavior to XtxtUML expressions.
	 */
	override protected isValueExpectedRecursive(XExpression expr) {
		val container = expr.eContainer;
		return switch (container) {
			TUAttribute,
			TUDeleteObjectExpression,
			TUSendSignalExpression,
			TULinkExpression,
			TUTransitionGuard: true
			XBlockExpression: false
			default: super.isValueExpectedRecursive(expr)
		}
	}

}
