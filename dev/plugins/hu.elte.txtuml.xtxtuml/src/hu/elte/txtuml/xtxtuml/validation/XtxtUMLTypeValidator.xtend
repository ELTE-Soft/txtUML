package hu.elte.txtuml.xtxtuml.validation;

import com.google.inject.Inject
import hu.elte.txtuml.api.model.DataType
import hu.elte.txtuml.api.model.ModelClass
import hu.elte.txtuml.api.model.ModelClass.Port
import hu.elte.txtuml.api.model.ModelEnum
import hu.elte.txtuml.api.model.Signal
import hu.elte.txtuml.xtxtuml.common.XtxtUMLConnectiveHelper
import hu.elte.txtuml.xtxtuml.common.XtxtUMLExternalityHelper
import hu.elte.txtuml.xtxtuml.common.XtxtUMLUtils
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAttributeOrOperationDeclarationPrefix
import hu.elte.txtuml.xtxtuml.xtxtUML.TUBindExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUBindType
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClassPropertyAccessExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnectiveEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnector
import hu.elte.txtuml.xtxtuml.xtxtUML.TUDeleteObjectExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TULogExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUPort
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
import org.eclipse.xtext.naming.QualifiedName
import org.eclipse.xtext.validation.Check
import org.eclipse.xtext.xbase.XExpression
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations

import static hu.elte.txtuml.xtxtuml.validation.XtxtUMLIssueCodes.*
import static hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage.Literals.*
import hu.elte.txtuml.xtxtuml.compiler.XtxtUMLBindExpressionAdapter

class XtxtUMLTypeValidator extends XtxtUMLUniquenessValidator {

	@Inject extension IJvmModelAssociations;
	@Inject extension IQualifiedNameProvider;
	@Inject extension XtxtUMLConnectiveHelper;
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
	def checkBindExpressionTypes(TUBindExpression bindExpr) {
		val inferredEnds = bindExpr.eAdapters.filter(XtxtUMLBindExpressionAdapter).head ?: {
			val newAdapter = new XtxtUMLBindExpressionAdapter;
			bindExpr.eAdapters.add(newAdapter);
			newAdapter
		}

		/* Do not allow any exceptions below as the compiler could fail otherwise. */

		if (!bindExpr.isBindExpressionConnectiveValid || !bindExpr.areBindExpressionEndsValid) {
			return;
		}

		val remainingParticipants = newHashSet(bindExpr.leftParticipant, bindExpr.rightParticipant);
		val Set<TUConnectiveEnd> remainingEnds = newHashSet(bindExpr.connective.connectiveEnds);

		val sides = newLinkedList(bindExpr.leftParticipant -> bindExpr.leftEnd -> TU_BIND_EXPRESSION__LEFT_END,
			bindExpr.rightParticipant -> bindExpr.rightEnd -> TU_BIND_EXPRESSION__RIGHT_END);
		if (bindExpr.leftEnd == null && bindExpr.rightEnd != null
				|| bindExpr.leftEnd == null && bindExpr.rightEnd == null
					&& remainingEnds.filter[bindExpr.leftParticipant.isCompatibleWith(it)].size > 1) {
			sides.reverse;
		}

		sides.forEach[ participantToEndToFeature |
			val givenParticipant = participantToEndToFeature.key.key;
			val givenEnd = participantToEndToFeature.key.value;
			val currentFeature = participantToEndToFeature.value;

			val registerMatchWith = [ TUConnectiveEnd actualEnd |
				remainingParticipants.remove(givenParticipant);
				remainingEnds.remove(actualEnd);
				inferredEnds.put(currentFeature, actualEnd)
			];

			if (givenEnd != null) {
				registerMatchWith.apply(givenEnd);
				if (!givenParticipant.isCompatibleWith(givenEnd)) {
					reportEndTypeMismatch(givenParticipant, givenEnd);
				}
			} else {
				val compatibleEnds = remainingEnds.filter[givenParticipant.isCompatibleWith(it)];
				if (compatibleEnds.size == 1) {
					registerMatchWith.apply(compatibleEnds.head);
				}
			}
		];

		if (remainingParticipants.size == 1) {
			reportEndTypeMismatch(remainingParticipants.head, remainingEnds.head);
		} else if (remainingParticipants.size > 1) {
			error("Cannot infer connective ends – please use the 'as' specifier at least on one side",
				bindExpr, null, AMBIGUOUS_BIND_EXPRESSION);
		}
	}

	def private isBindExpressionConnectiveValid(TUBindExpression it) {
		if (connective == null) {
			// syntax error is raised elsewhere
			return false;
		}

		if ((type == TUBindType.LINK || type == TUBindType.UNLINK) && !(connective instanceof TUAssociation)
				|| type == TUBindType.CONNECT && !(connective instanceof TUConnector)) {
			val expectedConnectiveKind = switch type {
				case TUBindType.LINK, case TUBindType.UNLINK: "an association"
				case TUBindType.CONNECT: "a connector"
			};

			error("Connective " + connective.connectiveName + " is not " + expectedConnectiveKind,
					it, TU_BIND_EXPRESSION__CONNECTIVE, CONNECTIVE_KIND_MISMATCH_IN_BIND_EXPRESSION);
			return false;
		}

		return true;
	}

	def private areBindExpressionEndsValid(TUBindExpression it) {
		if (connective.connectiveEnds.size != 2 || connective.connectiveEnds.exists[endEntity == null]
				|| #[leftEnd, rightEnd].exists[it != null && endEntity == null]) {
			// syntax error is raised elsewhere
			return false;
		}

		var result = true;

		result = result && #[leftEnd -> TU_BIND_EXPRESSION__LEFT_END, rightEnd -> TU_BIND_EXPRESSION__RIGHT_END].forall[ endToFeature |
			val currentEnd = endToFeature.key;
			val currentFeature = endToFeature.value;

			if (currentEnd != null && !connective.connectiveEnds.exists[fullyQualifiedName == currentEnd.fullyQualifiedName]) {
				error("End " + currentEnd.connectiveEndName + " does not belong to connective " + connective.connectiveName,
					it, currentFeature, END_MISMATCH_IN_BIND_EXPRESSION);
				return false;
			}

			return true;
		];

		if (leftEnd != null && rightEnd != null && leftEnd.fullyQualifiedName == rightEnd.fullyQualifiedName) {
			#[leftEnd -> TU_BIND_EXPRESSION__LEFT_END, rightEnd -> TU_BIND_EXPRESSION__RIGHT_END].forEach[ endToFeature |
				error("Duplicate connective end " + endToFeature.key.connectiveEndName, it, endToFeature.value,
					DUPLICATE_END_IN_BIND_EXPRESSION);
			];

			result = false;
		}

		return result;
	}

	def private isCompatibleWith(XExpression actualParticipant, TUConnectiveEnd expectedEnd) {
		// do not use getPrimaryJvmElement here, see 8c7a70b
		val actualSource = actualParticipant.actualType.type.getPrimarySourceElement;
		val expectedName = expectedEnd.endEntity?.fullyQualifiedName;
		return !actualParticipant.nullLiteral && internalIsCompatibleWith(actualSource, expectedName);
	}

	def private dispatch internalIsCompatibleWith(TUClass actualClass, QualifiedName expectedName) {
		actualClass.travelClassHierarchy[fullyQualifiedName == expectedName];
	}

	def private dispatch internalIsCompatibleWith(TUPort actualPort, QualifiedName expectedName) {
		actualPort.fullyQualifiedName == expectedName;
	}

	def private reportEndTypeMismatch(XExpression actualParticipant, TUConnectiveEnd expectedEnd) {
		val endEntity = expectedEnd.endEntity;
		val endEntityName = endEntity?.fullyQualifiedName.lastSegment
		val actualType = actualParticipant.actualType;
		val actualToExpected = if (actualType.humanReadableName.split("\\.").last == endEntityName) {
				actualType.identifier.replace('$', '.') + " to " + endEntity?.fullyQualifiedName
			} else {
				actualType.humanReadableName + " to " + endEntityName
			};

		error("Type mismatch: cannot convert from " + actualToExpected, actualParticipant, null, TYPE_MISMATCH);
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
