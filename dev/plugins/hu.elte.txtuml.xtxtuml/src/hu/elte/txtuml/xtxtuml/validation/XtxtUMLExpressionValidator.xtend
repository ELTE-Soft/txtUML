package hu.elte.txtuml.xtxtuml.validation

import com.google.inject.Inject
import hu.elte.txtuml.api.model.Port
import hu.elte.txtuml.api.model.Signal
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfDeleteObjectExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSendSignalExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSignalAccessExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClassPropertyAccessExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUPort
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignal
import hu.elte.txtuml.xtxtuml.xtxtUML.TUState
import hu.elte.txtuml.xtxtuml.xtxtUML.TUStateType
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransition
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionVertex
import hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage
import org.eclipse.xtext.common.types.JvmOperation
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.validation.Check
import org.eclipse.xtext.xbase.XAbstractFeatureCall
import org.eclipse.xtext.xbase.XBlockExpression
import org.eclipse.xtext.xbase.XExpression
import org.eclipse.xtext.xbase.XFeatureCall
import org.eclipse.xtext.xbase.XMemberFeatureCall
import org.eclipse.xtext.xbase.XbasePackage
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations
import org.eclipse.xtext.xbase.typesystem.util.ExtendedEarlyExitComputer

class XtxtUMLExpressionValidator extends XtxtUMLTypeValidator {

	@Inject extension ExtendedEarlyExitComputer;
	@Inject extension IQualifiedNameProvider;
	@Inject extension IJvmModelAssociations;

	@Check
	def checkMandatoryIntentionalReturn(TUOperation op) {
		val returnTypeName = op.prefix.type.type.fullyQualifiedName;
		if (returnTypeName.toString != "void" && !op.body.definiteEarlyExit) {
			error(
				"This method must return a result of type " + returnTypeName.lastSegment,
				XtxtUMLPackage::eINSTANCE.TUOperation_Name
			);
		}
	}

	@Check
	def checkNoExplicitExtensionCall(XFeatureCall featureCall) {
		internalCheckNoExplicitExtensionCall(featureCall);
	}

	@Check
	def checkNoExplicitExtensionCall(XMemberFeatureCall featureCall) {
		internalCheckNoExplicitExtensionCall(featureCall);
	}

	@Check
	def checkXtxtUMLExplicitOperationCall(XFeatureCall featureCall) {
		if (featureCall.feature instanceof JvmOperation && !featureCall.isExplicitOperationCall) {
			error(
				"Empty parentheses are required for methods without parameters",
				XbasePackage::eINSTANCE.XAbstractFeatureCall_Feature
			);
		}
	}

	@Check
	def checkXtxtUMLExplicitOperationCall(XMemberFeatureCall featureCall) {
		if (featureCall.feature instanceof JvmOperation && !featureCall.isExplicitOperationCall) {
			error(
				"Empty parentheses are required for methods without parameters",
				XbasePackage::eINSTANCE.XAbstractFeatureCall_Feature
			);
		}
	}

	@Check
	def checkSignalAccessExpression(RAlfSignalAccessExpression sigExpr) {
		var container = sigExpr.eContainer;
		while (container != null && !(container instanceof TUState) && !(container instanceof TUTransition)) {
			container = container.eContainer;
		}

		if (container == null || container instanceof TUState && (
				(container as TUState).type == TUStateType.INITIAL || (container as TUState).type == TUStateType.CHOICE
			) || container instanceof TUTransition && ((container as TUTransition).members.findFirst [
			it instanceof TUTransitionVertex && (it as TUTransitionVertex).from
		] as TUTransitionVertex)?.vertex?.type == TUStateType.INITIAL) {
			error(
				"'sigdata' cannot be used here",
				XtxtUMLPackage::eINSTANCE.RAlfSignalAccessExpression_Sigdata
			)
		}
	}

	@Check
	def checkSignalSentToPortIsRequired(RAlfSendSignalExpression sendExpr) {
		if (!sendExpr.target.isConformantWith(Port, false) || !sendExpr.signal.isConformantWith(Signal, false)) {
			return;
		}

		val sentSignalSourceElement = sendExpr.signal.actualType.type.primarySourceElement as TUSignal;

		val portSourceElement = sendExpr.target.actualType.type.primarySourceElement as TUPort;
		val requiredReceptionsOfPort = portSourceElement.members.findFirst[required]?.interface?.receptions;

		if (requiredReceptionsOfPort?.
			findFirst[signal.fullyQualifiedName == sentSignalSourceElement.fullyQualifiedName] == null) {
			error("Signal type " + sentSignalSourceElement.name + " is not required by port " + portSourceElement.name,
				XtxtUMLPackage::eINSTANCE.RAlfSendSignalExpression_Signal);
		}
	}

	@Check
	def checkOwnerOfAccessedClassProperty(TUClassPropertyAccessExpression accessExpr) {
		val leftSourceElement = accessExpr.left.actualType.type.primarySourceElement as TUClass;

		if (leftSourceElement == null) {
			return; // typechecks will mark it
		}

		switch (prop : accessExpr.right) {
			TUAssociationEnd: {
				val validAccessor = (prop.eContainer as TUAssociation).ends.findFirst[name != prop.name].endClass
				if (leftSourceElement.fullyQualifiedName != validAccessor.fullyQualifiedName) {
					error("Association end " + prop.name + " is not accessible from class " + leftSourceElement.name,
						XtxtUMLPackage::eINSTANCE.TUClassPropertyAccessExpression_Right);
				} else if (prop.notNavigable) {
					error("Association end " + prop.name + " is not navigable",
						XtxtUMLPackage::eINSTANCE.TUClassPropertyAccessExpression_Right);
				}
			}
			TUPort: {
				val validAccessor = (prop.eContainer as TUClass);
				if (leftSourceElement.fullyQualifiedName != validAccessor.fullyQualifiedName) {
					error(prop.name + " cannot be resolved as a port of class " + leftSourceElement.name,
						XtxtUMLPackage::eINSTANCE.TUClassPropertyAccessExpression_Right);
				}
			}
		}
	}

	/*
	 * TODO modify ExtensionScopeHelper
	 */
	def private internalCheckNoExplicitExtensionCall(XAbstractFeatureCall featureCall) {
		if (featureCall.isExtension) {
			val actualArgs = featureCall.
				actualArguments
			;
			error(
				'''The method «featureCall.feature.simpleName»(«actualArgs.drop(1).join(", ")[actualType.simpleName.replace("$", ".")]») is undefined for the type «actualArgs.head.actualType.simpleName.replace("$", ".")»''',
				XbasePackage::eINSTANCE.XAbstractFeatureCall_Feature
			);
		}
	}

	override protected isValueExpectedRecursive(XExpression expr) {
		val container = expr.eContainer;
		return switch (container) {
			RAlfSendSignalExpression,
			RAlfDeleteObjectExpression: true
			XBlockExpression: false
			default: super.isValueExpectedRecursive(expr)
		}
	}

}
