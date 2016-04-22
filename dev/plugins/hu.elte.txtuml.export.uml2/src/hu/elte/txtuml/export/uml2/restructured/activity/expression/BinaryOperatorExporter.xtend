package hu.elte.txtuml.export.uml2.restructured.activity.expression

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.export.uml2.restructured.activity.ActionExporter
import org.eclipse.jdt.core.dom.InfixExpression
import org.eclipse.jdt.core.dom.InfixExpression.Operator
import org.eclipse.uml2.uml.Action
import org.eclipse.uml2.uml.CallOperationAction
import org.eclipse.uml2.uml.Operation

class BinaryOperatorExporter extends ActionExporter<InfixExpression, CallOperationAction> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(InfixExpression access) { factory.createCallOperationAction }

	override exportContents(InfixExpression source) {
		val argType = source.leftOperand.resolveTypeBinding
		val operator = switch source.operator {
			case Operator.PLUS:
				if(argType.name == "int") plusOp else concatOp
			case Operator.MINUS:
				minusOp
			case Operator.TIMES:
				timesOp
			case Operator.DIVIDE:
				divideOp
			case Operator.REMAINDER:
				remainderOp
			case Operator.AND:
				andOp
			case Operator.OR:
				orOp
			case Operator.LESS:
				lessOp
			case Operator.GREATER:
				greaterOp
			case Operator.LESS_EQUALS:
				lessEqualsOp
			case Operator.GREATER_EQUALS:
				greaterEqualsOp
			case Operator.EQUALS:
				switch argType.name {
					case "int": integerEqOp
					case "bool": boolEqOp
					default: objectEqOp
				}
			case Operator.NOT_EQUALS:
				switch argType.name {
					case "int": integerNotEqualsOp
					case "bool": boolNotEqualsOp
					default: objectNotEqualsOp
				}
		}
		val left = exportExpression(source.leftOperand)
		val right = exportExpression(source.rightOperand)
		finishOperator(result, source.operator.toString, operator, left, right)
	}

	def minusOp() { getImportedOperation("IntegerOperations", "sub") }

	def plusOp() { getImportedOperation("IntegerOperations", "add") }

	def concatOp() { getImportedOperation("StringOperations", "concat") }

	def timesOp() { getImportedOperation("IntegerOperations", "mul") }

	def divideOp() { getImportedOperation("IntegerOperations", "div") }

	def remainderOp() { getImportedOperation("IntegerOperations", "mod") }

	def andOp() { getImportedOperation("BooleanOperations", "and") }

	def orOp() { getImportedOperation("BooleanOperations", "or") }

	def lessOp() { getImportedOperation("IntegerOperations", "lt") }

	def greaterOp() { getImportedOperation("IntegerOperations", "gt") }

	def lessEqualsOp() { getImportedOperation("IntegerOperations", "leq") }

	def greaterEqualsOp() { getImportedOperation("IntegerOperations", "geq") }

	def integerEqOp() { getImportedOperation("IntegerOperations", "eq") }

	def boolEqOp() { getImportedOperation("BooleanOperations", "eq") }

	def objectEqOp() { getImportedOperation("ObjectOperations", "eq") }

	def integerNotEqualsOp() { getImportedOperation("IntegerOperations", "neq") }

	def boolNotEqualsOp() { getImportedOperation("BooleanOperations", "neq") }

	def objectNotEqualsOp() { getImportedOperation("ObjectOperations", "neq") }

	def exportOperator(Operation operator, String symbol, Action left, Action right) {
		val ret = factory.createCallOperationAction
		finishOperator(ret, symbol, operator, left, right)
		storeNode(ret)
		return ret
	}

	protected def finishOperator(CallOperationAction expr, String symbol, Operation operator, Action left, Action right) {
		val returnType = operator.ownedParameters.findFirst[name == "return"].type
		expr.operation = operator
		expr.createResult("result", returnType)
		left.result.objectFlow(expr.createArgument("left", left.result.type))
		right.result.objectFlow(expr.createArgument("right", right.result.type))
		expr.name = '''«left.name»«symbol»«right.name»'''
	}
}