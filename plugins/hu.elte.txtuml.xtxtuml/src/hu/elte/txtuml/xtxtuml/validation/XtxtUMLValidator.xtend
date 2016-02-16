package hu.elte.txtuml.xtxtuml.validation

import com.google.inject.Inject
import hu.elte.txtuml.api.model.ModelClass
import hu.elte.txtuml.api.model.Signal
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfAssocNavExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfDeleteObjectExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSendSignalExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSignalAccessExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAttributeOrOperationDeclarationPrefix
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
import hu.elte.txtuml.xtxtuml.xtxtUML.TUFile
import hu.elte.txtuml.xtxtuml.xtxtUML.TUModelDeclaration
import hu.elte.txtuml.xtxtuml.xtxtUML.TUModelElement
import hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignal
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUState
import hu.elte.txtuml.xtxtuml.xtxtUML.TUStateType
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransition
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionGuard
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionVertex
import hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage
import java.util.HashSet
import java.util.List
import java.util.Map
import org.eclipse.xtext.common.types.JvmFormalParameter
import org.eclipse.xtext.common.types.JvmOperation
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.common.types.TypesPackage
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.validation.Check
import org.eclipse.xtext.xbase.XAbstractFeatureCall
import org.eclipse.xtext.xbase.XBlockExpression
import org.eclipse.xtext.xbase.XExpression
import org.eclipse.xtext.xbase.XFeatureCall
import org.eclipse.xtext.xbase.XMemberFeatureCall
import org.eclipse.xtext.xbase.XbasePackage
import org.eclipse.xtext.xbase.typesystem.util.ExtendedEarlyExitComputer
import org.eclipse.xtext.xtype.XImportDeclaration

import static org.eclipse.xtext.xbase.validation.IssueCodes.IMPORT_UNUSED

class XtxtUMLValidator extends XtxtUMLAssociationValidator {

	@Inject extension ExtendedEarlyExitComputer;
	@Inject extension IQualifiedNameProvider;

	// Checks
	@Check
	def checkModelDeclarationIsInModelInfoFile(TUModelDeclaration modelDeclaration) {
		var name = modelDeclaration.eResource?.URI.lastSegment ?: ""
		if (!"model-info.xtxtuml".equals(name)) {
			error('Model declaration must be specified in "model-info.xtxtuml".', modelDeclaration, null)
		}
	}

	@Check
	def checkNoDuplicateFileElementExternal(TUModelElement modelElement) {
		try {
			Class.forName(modelElement.fullyQualifiedName.toString, false, getClass.getClassLoader);

			// class with the same qualified name is found
			error(
				"Duplicate file element " + modelElement.name,
				XtxtUMLPackage::eINSTANCE.TUModelElement_Name
			);
		} catch (ClassNotFoundException ex) {
			// no problem
		}
	}

	@Check
	def CheckNoDuplicateFileElementInternal(TUModelElement modelElement) {
		val siblingsAndSelf = (modelElement.eContainer as TUFile).elements;
		if (siblingsAndSelf.exists [
			name == modelElement.name && it != modelElement
		]) {
			error(
				"Duplicate file element " + modelElement.name,
				XtxtUMLPackage::eINSTANCE.TUModelElement_Name
			);
		}
	}

	/*
	 * TODO remove when general TUAttribute is introduced
	 * TODO override local variable shadowing check defined in AbstractTypeComputationState
	 */
	@Check
	def checkNoDuplicateSignalAttribute(TUSignalAttribute attr) {
		val containingSignal = attr.eContainer as TUSignal;
		if (containingSignal.attributes.exists [
			name == attr.name && it != attr
		]) {
			error(
				"Duplicate attribute " + attr.name + " in signal " + containingSignal.name,
				XtxtUMLPackage::eINSTANCE.TUSignalAttribute_Name
			);
		}
	}

	@Check
	def checkNoDuplicateAttribute(TUAttribute attr) {
		val containingClass = attr.eContainer as TUClass;
		if (containingClass.members.exists [
			it instanceof TUAttribute && (it as TUAttribute).name == attr.name && it != attr
		]) {
			error(
				"Duplicate attribute " + attr.name + " in class " + containingClass.name,
				XtxtUMLPackage::eINSTANCE.TUAttribute_Name
			);
		}
	}

	@Check
	def checkNoDuplicateOperation(TUOperation op) {
		val containingClass = (op.eContainer as TUClass);
		if (containingClass.members.exists [
			it instanceof TUOperation && {
				val siblingOrSelfOp = it as TUOperation;
				siblingOrSelfOp.name == op.name && siblingOrSelfOp.parameterTypeList == op.parameterTypeList

			} && it != op
		]) {
			error(
				'''Duplicate method «op.name»(«op.parameterTypeList.join(", ")»)''',
				XtxtUMLPackage.eINSTANCE.TUOperation_Name
			);
		}
	}

	@Check
	def checkNoDuplicateState(TUState state) {
		val container = state.eContainer;
		val siblingsAndSelf = if (container instanceof TUClass) {
				(container as TUClass).members
			} else {
				(container as TUState).members
			}

		if (siblingsAndSelf.exists [
			it instanceof TUState && (it as TUState).name == state.name && it != state ||
				it instanceof TUTransition && (it as TUTransition).name == state.name
		]) {
			error(
				"Duplicate element " + state.name,
				XtxtUMLPackage::eINSTANCE.TUState_Name
			);
		}
	}

	@Check
	def checkNoDuplicateTransition(TUTransition trans) {
		val container = trans.eContainer;
		val siblingsAndSelf = if (container instanceof TUClass) {
				(container as TUClass).members
			} else {
				(container as TUState).members
			}

		if (siblingsAndSelf.exists [
			it instanceof TUTransition && (it as TUTransition).name == trans.name && it != trans ||
				it instanceof TUState && (it as TUState).name == trans.name
		]) {
			error(
				"Duplicate element " + trans.name,
				XtxtUMLPackage::eINSTANCE.TUTransition_Name
			);
		}

	}

	@Check
	def checkNoCycleInClassHiearchy(TUClass tUClass) {
		if (tUClass.superClass == null) {
			return;
		}

		val visitedClasses = new HashSet<TUClass>();
		visitedClasses.add(tUClass);

		var currentClass = tUClass.superClass;
		while (currentClass != null) {
			if (visitedClasses.contains(currentClass)) {
				error(
					"Cycle in hierarchy of class " + tUClass.name + " reaching " + currentClass.name,
					XtxtUMLPackage::eINSTANCE.TUClass_SuperClass,
					IssueCodes.CLASS_HIERARCHY_CYCLE,
					currentClass.name
				);

				return;
			}

			visitedClasses.add(currentClass);
			currentClass = currentClass.superClass;
		}
	}

	@Check
	def checkTypeReference(JvmTypeReference typeRef) {
		var isPartOfSignalAttribute = false;

		val isValid = switch (container : typeRef.eContainer) {
			TUSignalAttribute: {
				isPartOfSignalAttribute = true;
				typeRef.isAllowedBasictype(false)
			}
			TUAttributeOrOperationDeclarationPrefix: {
				typeRef.isAllowedBasictype(container.eContainer instanceof TUOperation) ||
					typeRef.isConformantWith(ModelClass)
			}
			JvmFormalParameter: {
				typeRef.isAllowedBasictype(false) || typeRef.isConformantWith(ModelClass)
			}
			// TODO check types inside XBlockExpression
			default:
				true
		}

		if (!isValid) {
			error(
				if (isPartOfSignalAttribute) {
					"Invalid type. Only boolean, double, int and String are allowed."
				} else {
					"Invalid type. Only boolean, double, int, String and model class types are allowed."
				},
				TypesPackage::eINSTANCE.jvmParameterizedTypeReference_Type
			)
		}
	}

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
	def checkSendSignalExpressionTypes(RAlfSendSignalExpression sendExpr) {
		if (!sendExpr.signal.isConformantWith(Signal, false)) {
			error(
				typeMismatch("Signal"),
				XtxtUMLPackage::eINSTANCE.RAlfSendSignalExpression_Signal
			);
		}

		if (!sendExpr.target.isConformantWith(ModelClass, false)) {
			error(
				typeMismatch("Class"),
				XtxtUMLPackage::eINSTANCE.RAlfSendSignalExpression_Target
			);
		}
	}

	@Check
	def checkDeleteObjectExpressionTypes(RAlfDeleteObjectExpression deleteExpr) {
		if (!deleteExpr.object.isConformantWith(ModelClass, false)) {
			error(
				typeMismatch("Class"),
				XtxtUMLPackage::eINSTANCE.RAlfDeleteObjectExpression_Object
			);
		}
	}

	@Check
	def checkAssocNavExpressionTypes(RAlfAssocNavExpression navExpr) {
		// TODO modify when empty collections are available in the api
		if (!navExpr.left.isConformantWith(ModelClass, false)) {
			error(
				typeMismatch("Class"),
				XtxtUMLPackage::eINSTANCE.RAlfAssocNavExpression_Left
			)
		}
	}

	@Check
	def checkElseGuard(TUTransitionGuard guard) {
		if (guard.^else && guard.eContainer instanceof TUTransition && (guard.eContainer as TUTransition).members.exists [
			it instanceof TUTransitionVertex && (it as TUTransitionVertex).from &&
				(it as TUTransitionVertex).vertex.type != TUStateType.CHOICE
		]) {
			error(
				"'else' condition can be used only if the source of the transition is a choice pseudostate",
				XtxtUMLPackage::eINSTANCE.TUTransitionGuard_Else
			)
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
		] as TUTransitionVertex)?.vertex.type == TUStateType.INITIAL) {
			error(
				"'sigdata' cannot be used here",
				XtxtUMLPackage::eINSTANCE.RAlfSignalAccessExpression_Sigdata
			)
		}
	}

	// Overriden Xbase validation methods
	override isValueExpectedRecursive(XExpression expr) {
		val container = expr.eContainer;
		return switch (container) {
			RAlfSendSignalExpression,
			RAlfDeleteObjectExpression: true
			XBlockExpression: false
			default: super.isValueExpectedRecursive(expr)
		}
	}

	override addImportUnusedIssues(Map<String, List<XImportDeclaration>> imports) {
		for (List<XImportDeclaration> importDeclarations : imports.values()) {
			for (XImportDeclaration importDeclaration : importDeclarations) {
				addIssue("The import '" + importDeclaration.importedType.getQualifiedName(".") + "' is never used.",
					importDeclaration, IMPORT_UNUSED);
			}
		}
	}

	// Helpers
	def private isAllowedBasictype(JvmTypeReference typeRef, boolean isVoidAllowed) {
		typeRef.isType(Integer.TYPE) || typeRef.isType(Boolean.TYPE) || typeRef.isType(Double.TYPE) ||
			typeRef.isType(String) || typeRef.isType(Void.TYPE) && isVoidAllowed
	}

	def private parameterTypeList(TUOperation op) {
		op.parameters.map[parameterType.type.fullyQualifiedName]
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

	def private isType(JvmTypeReference typeRef, Class<?> expectedType) {
		typeRef.toLightweightTypeReference.isType(expectedType);
	}

	def private isConformantWith(JvmTypeReference typeRef, Class<?> expectedType) {
		typeRef.toLightweightTypeReference.isSubtypeOf(expectedType);
	}

	def private isConformantWith(XExpression expr, Class<?> expectedType, boolean isNullAllowed) {
		expr.actualType.isSubtypeOf(expectedType) && (isNullAllowed || !isNullLiteral(expr))
	}

	def private isNullLiteral(XExpression expr) {
		expr.actualType.canonicalName == "null";
	}

	def private typeMismatch(String expectedType) {
		"Type mismatch: cannot convert the expression to " + expectedType
	}

}
