package hu.elte.txtuml.xtxtuml.validation;

import com.google.inject.Inject
import hu.elte.txtuml.api.model.DataType
import hu.elte.txtuml.api.model.ModelClass
import hu.elte.txtuml.api.model.ModelClass.Port
import hu.elte.txtuml.api.model.ModelEnum
import hu.elte.txtuml.api.model.Signal
import hu.elte.txtuml.xtxtuml.common.XtxtUMLExternalityHelper
import hu.elte.txtuml.xtxtuml.common.XtxtUMLUtils
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAttributeOrOperationDeclarationPrefix
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClassPropertyAccessExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUDeleteObjectExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TULinkExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TULogExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSendSignalExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUStartObjectExpression
import java.util.Set
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EStructuralFeature
import org.eclipse.xtext.common.types.JvmFormalParameter
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.common.types.TypesPackage
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.validation.Check
import org.eclipse.xtext.xbase.XExpression
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations

import static hu.elte.txtuml.xtxtuml.validation.XtxtUMLIssueCodes.*
import static hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage.Literals.*

class XtxtUMLTypeValidator extends XtxtUMLUniquenessValidator {

	@Inject extension IJvmModelAssociations;
	@Inject extension IQualifiedNameProvider;
	@Inject extension XtxtUMLExternalityHelper;
	@Inject extension XtxtUMLUtils;

	@Check
	def checkTypeReference(JvmTypeReference typeRef) {
		if (typeRef.external) return;

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
					"Invalid type. Only boolean, double, int, String, model enums and model data types are allowed."
				} else {
					"Invalid type. Only boolean, double, int, String, model enums, model data types, signal and model class types are allowed."
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
	def checkStartObjectExpressionTypes(TUStartObjectExpression startExpr) {
		if (!startExpr.object.isConformantWith(ModelClass, false)) {
			typeMismatch("Class", startExpr, TU_START_OBJECT_EXPRESSION__OBJECT)
		}
	}

	@Check
	def checkDeleteObjectExpressionTypes(TUDeleteObjectExpression deleteExpr) {
		if (!deleteExpr.object.isConformantWith(ModelClass, false)) {
			typeMismatch("Class", deleteExpr, TU_DELETE_OBJECT_EXPRESSION__OBJECT)
		}
	}

	@Check
	def checkLogExpressionType(TULogExpression logExpr) {
		if (!logExpr.log.isConformantWith(String, false)) {
			typeMismatch("String", logExpr, TU_LOG_EXPRESSION__LOG)
		}
	}

	@Check
	def checkClassPropertyAccessExpressionTypes(TUClassPropertyAccessExpression accessExpr) {
		if (!accessExpr.left.isConformantWith(ModelClass, false)) {
			typeMismatch("Class", accessExpr, TU_CLASS_PROPERTY_ACCESS_EXPRESSION__LEFT)
		}
	}

	@Check
	def checkLinkExpressionTypes(TULinkExpression linkExpr) {
		/* Do not allow any exceptions below as the compiler could fail otherwise. */

		if (!linkExpr.areLinkExpressionEndsValid) {
			return;
		}

		val remainingObjects = newHashSet(linkExpr.leftObject, linkExpr.rightObject);
		val Set<TUAssociationEnd> remainingEnds = newHashSet(linkExpr.association.ends);

		val sides = newLinkedList(linkExpr.leftObject -> linkExpr.leftEnd -> TU_LINK_EXPRESSION__LEFT_END,
			linkExpr.rightObject -> linkExpr.rightEnd -> TU_LINK_EXPRESSION__RIGHT_END);
		if (linkExpr.leftEnd == null && linkExpr.rightEnd != null
				|| linkExpr.leftEnd == null && linkExpr.rightEnd == null
					&& remainingEnds.filter[linkExpr.leftObject.isCompatibleWith(it)].size > 1) {
			sides.reverse;
		}

		sides.forEach[ objectToEndToFeature |
			val givenObject = objectToEndToFeature.key.key;
			val givenEnd = objectToEndToFeature.key.value;
			val currentFeature = objectToEndToFeature.value;

			val registerMatchWith = [ TUAssociationEnd actualEnd |
				remainingObjects.remove(givenObject);
				remainingEnds.remove(actualEnd);

				if (linkExpr.leftEnd == null && currentFeature == TU_LINK_EXPRESSION__LEFT_END) {
					linkExpr.leftEnd = actualEnd;
				} else if (linkExpr.rightEnd == null && currentFeature == TU_LINK_EXPRESSION__RIGHT_END) {
					linkExpr.rightEnd = actualEnd;
				}
			];

			if (givenEnd != null) {
				registerMatchWith.apply(givenEnd);
				if (!givenObject.isCompatibleWith(givenEnd)) {
					reportEndTypeMismatch(givenObject, givenEnd);
				}
			} else {
				val compatibleEnds = remainingEnds.filter[givenObject.isCompatibleWith(it)];
				if (compatibleEnds.size == 1) {
					registerMatchWith.apply(compatibleEnds.head);
				}
			}
		];

		if (remainingObjects.size == 1) {
			reportEndTypeMismatch(remainingObjects.head, remainingEnds.head);
		} else if (remainingObjects.size > 1) {
			error("Cannot infer association ends – please use the 'as' specifier at least on one side",
				linkExpr, null, AMBIGUOUS_LINK_EXPRESSION);
		}
	}

	def private areLinkExpressionEndsValid(TULinkExpression it) {
		if (association == null || association.ends.size != 2 || association.ends.exists[endClass == null]
				|| #[leftEnd, rightEnd].exists[it != null && endClass == null]) {
			// syntax error is raised elsewhere
			return false;
		}

		var result = true;

		if (leftEnd == null && rightEnd == null
				&& association.ends.head.endClass.fullyQualifiedName == association.ends.last.endClass.fullyQualifiedName) {
			error("Cannot infer ends in case of a reflexive association – please use the 'as' specifier at least on one side",
				it, null, UNSPECIFIED_REFLEXIVE_ENDS);
			result = false;
		}

		result = result && #[leftEnd -> TU_LINK_EXPRESSION__LEFT_END, rightEnd -> TU_LINK_EXPRESSION__RIGHT_END].forall[ endToFeature |
			if (endToFeature.key != null && !association.ends.exists[fullyQualifiedName == endToFeature.key.fullyQualifiedName]) {
				error("End " + endToFeature.key.name + " does not belong to association " + association.name,
					it, endToFeature.value, END_MISMATCH_IN_LINK_EXPRESSION);
				return false;
			}

			return true;
		];

		if (leftEnd != null && rightEnd != null && leftEnd.fullyQualifiedName == rightEnd.fullyQualifiedName) {
			#[leftEnd -> TU_LINK_EXPRESSION__LEFT_END, rightEnd -> TU_LINK_EXPRESSION__RIGHT_END].forEach[ endToFeature |
				error("Duplicate association end " + endToFeature.key.name, it, endToFeature.value, DUPLICATE_END_IN_LINK_EXPRESSION);
			];

			result = false;
		}

		return result;
	}

	def private isCompatibleWith(XExpression actualObject, TUAssociationEnd expectedEnd) {
		// do not use getPrimaryJvmElement here, see 8c7a70b
		val actualClass = actualObject.actualType.type.getPrimarySourceElement;
		val expectedClassName = expectedEnd.endClass.fullyQualifiedName;
		return !actualObject.nullLiteral && actualClass instanceof TUClass && (actualClass as TUClass).travelClassHierarchy[fullyQualifiedName == expectedClassName];
	}

	def private reportEndTypeMismatch(XExpression actualObject, TUAssociationEnd expectedEnd) {
		val endClass = expectedEnd.endClass;
		val actualType = actualObject.actualType;
		val actualToExpected = if (actualType.humanReadableName == endClass.name) {
				actualType.identifier + " to " + endClass.fullyQualifiedName
			} else {
				actualType.humanReadableName + " to " + endClass.name
			};

		error("Type mismatch: cannot convert from " + actualToExpected, actualObject, null, TYPE_MISMATCH);
	}

	def protected isAllowedParameterType(JvmTypeReference typeRef, boolean isVoidAllowed) {
		isAllowedAttributeType(typeRef, isVoidAllowed) || typeRef.isConformantWith(ModelClass) ||
			typeRef.isConformantWith(Signal)
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
