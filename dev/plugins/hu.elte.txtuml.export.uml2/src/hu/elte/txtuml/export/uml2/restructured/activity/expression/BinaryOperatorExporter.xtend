package hu.elte.txtuml.export.uml2.restructured.activity.expression

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.export.uml2.restructured.activity.ActionExporter
import org.eclipse.jdt.core.dom.InfixExpression
import org.eclipse.jdt.core.dom.InfixExpression.Operator
import org.eclipse.uml2.uml.CallOperationAction

class BinaryOperatorExporter extends ActionExporter<InfixExpression, CallOperationAction> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(InfixExpression access) { factory.createCallOperationAction }

	override exportContents(InfixExpression source) {
		val argType = source.leftOperand.resolveTypeBinding
		val operator = switch source.operator {
			case Operator.PLUS:
				if (argType.name == "int") {
					getImportedOperation("IntegerOperations", "add")
				} else {
					getImportedOperation("StringOperations", "concat")
				}
			case Operator.MINUS:
				getImportedOperation("IntegerOperations", "sub")
			case Operator.TIMES:
				getImportedOperation("IntegerOperations", "mul")
			case Operator.DIVIDE:
				getImportedOperation("IntegerOperations", "div")
			case Operator.REMAINDER:
				getImportedOperation("IntegerOperations", "mod")
			case Operator.AND:
				getImportedOperation("BooleanOperations", "and")
			case Operator.OR:
				getImportedOperation("BooleanOperations", "or")
			case Operator.LESS:
				getImportedOperation("BooleanOperations", "lt")
			case Operator.GREATER:
				getImportedOperation("BooleanOperations", "gt")
			case Operator.LESS_EQUALS:
				getImportedOperation("BooleanOperations", "leq")
			case Operator.GREATER_EQUALS:
				getImportedOperation("BooleanOperations", "geq")
			case Operator.EQUALS:
				switch argType.name {
					case "int": getImportedOperation("IntegerOperations", "eq")
					case "bool": getImportedOperation("BooleanOperations", "eq")
					default: getImportedOperation("ObjectOperations", "eq")
				}
			case Operator.NOT_EQUALS:
				switch argType.name {
					case "int": getImportedOperation("IntegerOperations", "neq")
					case "bool": getImportedOperation("BooleanOperations", "neq")
					default: getImportedOperation("ObjectOperations", "neq")
				}
		}
		val returnType = operator.ownedParameters.findFirst[name == "return"].type
		val left = exportExpression(source.leftOperand)
		val right = exportExpression(source.rightOperand)
		result.operation = operator
		result.createResult("result", returnType)
		left.result.objectFlow(result.createArgument("left", left.result.type))
		right.result.objectFlow(result.createArgument("right", right.result.type))
		result.name = '''«left.name»«source.operator»«right.name»'''
	}
}