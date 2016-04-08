package hu.elte.txtuml.xtxtuml.validation

import hu.elte.txtuml.api.model.DataType
import hu.elte.txtuml.api.model.ModelClass
import hu.elte.txtuml.api.model.Port
import hu.elte.txtuml.api.model.Signal
import hu.elte.txtuml.api.model.external.ExternalType
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfDeleteObjectExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSendSignalExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAttributeOrOperationDeclarationPrefix
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClassPropertyAccessExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage
import org.eclipse.xtext.common.types.JvmFormalParameter
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.common.types.TypesPackage
import org.eclipse.xtext.validation.Check
import org.eclipse.xtext.xbase.XExpression

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
					"Invalid type. Only boolean, double, int, String, model data types and external interfaces are allowed."
				} else {
					"Invalid type. Only boolean, double, int, String, model data types, external interfaces and model class types are allowed."
				},
				TypesPackage::eINSTANCE.jvmParameterizedTypeReference_Type
			)
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

		if (!sendExpr.target.isConformantWith(ModelClass, false) && !sendExpr.target.isConformantWith(Port, false)) {
			error(
				typeMismatch("Class or Port"),
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
	def checkClassPropertyAccessExpressionTypes(TUClassPropertyAccessExpression accessExpr) {
		if (!accessExpr.left.isConformantWith(ModelClass, false)) {
			error(
				typeMismatch("Class"),
				XtxtUMLPackage::eINSTANCE.TUClassPropertyAccessExpression_Left
			)
		}
	}

	def protected isAllowedParameterType(JvmTypeReference typeRef, boolean isVoidAllowed) {
		isAllowedAttributeType(typeRef, isVoidAllowed) || typeRef.isConformantWith(ModelClass)
	}

	def protected isAllowedAttributeType(JvmTypeReference typeRef, boolean isVoidAllowed) {
		isAllowedBasicType(typeRef, isVoidAllowed) || typeRef.isConformantWith(DataType) ||
			typeRef.type.interface && typeRef.isConformantWith(ExternalType)
	}

	def protected isAllowedBasicType(JvmTypeReference typeRef, boolean isVoidAllowed) {
		typeRef.isType(Integer.TYPE) || typeRef.isType(Boolean.TYPE) || typeRef.isType(Double.TYPE) ||
			typeRef.isType(String) || typeRef.isType(Void.TYPE) && isVoidAllowed
	}

	def protected isType(JvmTypeReference typeRef, Class<?> expectedType) {
		typeRef.toLightweightTypeReference.isType(expectedType);
	}

	def protected isConformantWith(JvmTypeReference typeRef, Class<?> expectedType) {
		typeRef.toLightweightTypeReference.isSubtypeOf(expectedType);
	}

	def protected isConformantWith(XExpression expr, Class<?> expectedType, boolean isNullAllowed) {
		expr.actualType.isSubtypeOf(expectedType) && (isNullAllowed || !isNullLiteral(expr))
	}

	def protected isNullLiteral(XExpression expr) {
		expr.actualType.canonicalName == "null";
	}

	def protected typeMismatch(String expectedType) {
		"Type mismatch: cannot convert the expression to " + expectedType
	}

}
