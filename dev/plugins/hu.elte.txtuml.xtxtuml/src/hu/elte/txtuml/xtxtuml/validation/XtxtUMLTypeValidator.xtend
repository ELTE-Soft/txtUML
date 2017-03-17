package hu.elte.txtuml.xtxtuml.validation;

import hu.elte.txtuml.api.model.DataType
import hu.elte.txtuml.api.model.ModelClass
import hu.elte.txtuml.api.model.ModelClass.Port
import hu.elte.txtuml.api.model.ModelEnum
import hu.elte.txtuml.api.model.Signal
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAttributeOrOperationDeclarationPrefix
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClassPropertyAccessExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUDeleteObjectExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSendSignalExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAttribute
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EStructuralFeature
import org.eclipse.xtext.common.types.JvmFormalParameter
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.common.types.TypesPackage
import org.eclipse.xtext.validation.Check
import org.eclipse.xtext.xbase.XExpression

import static hu.elte.txtuml.xtxtuml.validation.XtxtUMLIssueCodes.*
import static hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage.Literals.*

class XtxtUMLTypeValidator extends XtxtUMLUniquenessValidator {

	@Check
	def checkTypeReference(JvmTypeReference typeRef) {
		var isAttribute = false;
		val isValid = switch (container : typeRef.eContainer) {
			TUSignalAttribute: {
				isAttribute = true;
				typeRef.isAllowedAttributeType(false)
			}
			TUAttributeOrOperationDeclarationPrefix: {
				if (container.eContainer instanceof TUOperation) {
					typeRef.isAllowedParameterType(true)
				} else {
					isAttribute = true;
					typeRef.isAllowedAttributeType(false)
				}
			}
			JvmFormalParameter: {
				typeRef.isAllowedParameterType(false)
			}
			// TODO check types inside XBlockExpression
			default:
				true
		}

		if (!isValid) {
			error(
				if (isAttribute) {
					"Invalid type. Only boolean, double, int, String, model enums, model data types and external interfaces are allowed."
				} else {
					"Invalid type. Only boolean, double, int, String, model enums, model data types, external interfaces and model class types are allowed."
				}, typeRef, TypesPackage.Literals.JVM_PARAMETERIZED_TYPE_REFERENCE__TYPE, INVALID_TYPE);
		}
	}

	@Check
	def checkSendSignalExpressionTypes(TUSendSignalExpression sendExpr) {
		if (!sendExpr.signal.isConformantWith(Signal, false)) {
			typeMismatch("Signal", sendExpr, TU_SEND_SIGNAL_EXPRESSION__SIGNAL);
		}

		if (!sendExpr.target.isConformantWith(ModelClass, false) && !sendExpr.target.isConformantWith(Port, false)) {
			typeMismatch("Class or Port", sendExpr, TU_SEND_SIGNAL_EXPRESSION__TARGET);
		}
	}

	@Check
	def checkDeleteObjectExpressionTypes(TUDeleteObjectExpression deleteExpr) {
		if (!deleteExpr.object.isConformantWith(ModelClass, false)) {
			typeMismatch("Class", deleteExpr, TU_DELETE_OBJECT_EXPRESSION__OBJECT)
		}
	}

	@Check
	def checkClassPropertyAccessExpressionTypes(TUClassPropertyAccessExpression accessExpr) {
		if (!accessExpr.left.isConformantWith(ModelClass, false)) {
			typeMismatch("Class", accessExpr, TU_CLASS_PROPERTY_ACCESS_EXPRESSION__LEFT)
		}
	}

	def protected isAllowedParameterType(JvmTypeReference typeRef, boolean isVoidAllowed) {
		isAllowedAttributeType(typeRef, isVoidAllowed) || typeRef.isConformantWith(ModelClass)
	}

	def protected isAllowedAttributeType(JvmTypeReference typeRef, boolean isVoidAllowed) {
		isAllowedBasicType(typeRef, isVoidAllowed) || typeRef.isConformantWith(DataType) ||
			typeRef.isConformantWith(ModelEnum)
	}

	def protected isAllowedBasicType(JvmTypeReference typeRef, boolean isVoidAllowed) {
		typeRef.isType(Integer.TYPE) || typeRef.isType(Boolean.TYPE) || typeRef.isType(Double.TYPE) ||
			typeRef.isType(String) || typeRef.isType(Void.TYPE) && isVoidAllowed
	}

	def protected isType(JvmTypeReference typeRef, Class<?> expectedType) {
		typeRef.toLightweightTypeReference.isType(expectedType)
	}

	def protected isConformantWith(JvmTypeReference typeRef, Class<?> expectedType) {
		typeRef.toLightweightTypeReference.isSubtypeOf(expectedType)
	}

	def protected isConformantWith(XExpression expr, Class<?> expectedType, boolean isNullAllowed) {
		expr != null && expr.actualType.isSubtypeOf(expectedType) && (isNullAllowed || !isNullLiteral(expr))
	}

	def protected isNullLiteral(XExpression expr) {
		expr != null && expr.actualType.canonicalName == "null"
	}

	def protected typeMismatch(String expectedType, EObject source, EStructuralFeature feature) {
		error("Type mismatch: cannot convert the expression to " + expectedType, source, feature, TYPE_MISMATCH)
	}

}
