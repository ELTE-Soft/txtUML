package hu.elte.txtuml.export.uml2.activity.expression

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.activity.ActionExporter
import hu.elte.txtuml.export.uml2.activity.apicalls.ToStringExporter
import org.eclipse.jdt.core.dom.InfixExpression
import org.eclipse.jdt.core.dom.InfixExpression.Operator
import org.eclipse.uml2.uml.Action
import org.eclipse.uml2.uml.CallOperationAction
import org.eclipse.uml2.uml.Operation
import static hu.elte.txtuml.export.uml2.activity.expression.OperatorType.*;

enum OperatorType {
	INTEGER,
	REAL,
	BOOLEAN,
	STRING,
	OBJECT
}

class BinaryOperatorExporter extends ActionExporter<InfixExpression, CallOperationAction> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(InfixExpression access) { factory.createCallOperationAction }

	override exportContents(InfixExpression source) {
		val argType = source.leftOperand.resolveTypeBinding

		val optype = switch argType.name {
			case "int",
			case "Int",
			case "long",
			case "Long",
			case "short",
			case "Short",
			case "byte",
			case "Byte": INTEGER
			case "double",
			case "Double",
			case "float",
			case "Float": REAL
			case "boolean",
			case "Boolean": BOOLEAN
			case "String": STRING
			default: OBJECT
		}

		val operator = switch #[source.operator, optype] {
			case #[Operator.PLUS, INTEGER]:
				plusOp
			case #[Operator.PLUS, REAL]:
				realPlusOp
			case #[Operator.PLUS, STRING]:
				concatOp
			case #[Operator.MINUS, INTEGER]:
				minusOp
			case #[Operator.MINUS, REAL]:
				realMinusOp
			case #[Operator.TIMES, INTEGER]:
				timesOp
			case #[Operator.TIMES, REAL]:
				realTimesOp
			case #[Operator.DIVIDE, INTEGER]:
				divideOp
			case #[Operator.DIVIDE, REAL]:
				realDivideOp
			case #[Operator.REMAINDER, INTEGER]:
				remainderOp
			case #[Operator.CONDITIONAL_AND, BOOLEAN]:
				andOp
			case #[Operator.CONDITIONAL_OR, BOOLEAN]:
				orOp
			case #[Operator.LESS, INTEGER]:
				lessOp
			case #[Operator.LESS, REAL]:
				realLessOp
			case #[Operator.GREATER, INTEGER]:
				greaterOp
			case #[Operator.GREATER, REAL]:
				realGreaterOp
			case #[Operator.LESS_EQUALS, INTEGER]:
				lessEqualsOp
			case #[Operator.LESS_EQUALS, REAL]:
				realLessEqualsOp
			case #[Operator.GREATER_EQUALS, INTEGER]:
				greaterEqualsOp
			case #[Operator.GREATER_EQUALS, REAL]:
				realGreaterEqualsOp
			case #[Operator.NOT_EQUALS, INTEGER]:
				integerNotEqualsOp
			case #[Operator.NOT_EQUALS, REAL]:
				realNotEqualsOp
			case #[Operator.NOT_EQUALS, BOOLEAN]:
				boolNotEqualsOp
			case #[Operator.NOT_EQUALS, STRING],
			case #[Operator.NOT_EQUALS, OBJECT]:
				objectNotEqualsOp
			default:
				throw new RuntimeException("Unrecognized operator: " + source.operator + " for operator type " + optype)
		}
		if (operator == concatOp) {
			handleAutoConversion(source, operator)
		} else {
			val left = exportExpression(source.leftOperand)
			val right = exportExpression(source.rightOperand)
			finishOperator(result, source.operator.toString, operator, left, right)
		}
	}

	def handleAutoConversion(InfixExpression source, Operation operator) {
		val left = autoToString(exportExpression(source.leftOperand))
		val right = autoToString(exportExpression(source.rightOperand))
		finishOperator(result, source.operator.toString, operator, left, right)
	}

	def autoToString(Action action) {
		new ToStringExporter(this).createToString(action)
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

	def integerNotEqualsOp() { getImportedOperation("IntegerOperations", "neq") }

	def boolNotEqualsOp() { getImportedOperation("BooleanOperations", "neq") }

	def objectNotEqualsOp() { getImportedOperation("ObjectOperations", "neq") }

	def realMinusOp() { getImportedOperation("RealOperations", "sub") }

	def realPlusOp() { getImportedOperation("RealOperations", "add") }

	def realTimesOp() { getImportedOperation("RealOperations", "mul") }

	def realDivideOp() { getImportedOperation("RealOperations", "div") }

	def realLessOp() { getImportedOperation("RealOperations", "lt") }

	def realGreaterOp() { getImportedOperation("RealOperations", "gt") }

	def realLessEqualsOp() { getImportedOperation("RealOperations", "leq") }

	def realGreaterEqualsOp() { getImportedOperation("RealOperations", "geq") }

	def realNotEqualsOp() { getImportedOperation("RealOperations", "neq") }

	def exportOperator(Operation operator, String symbol, Action left, Action right) {
		val ret = factory.createCallOperationAction
		finishOperator(ret, symbol, operator, left, right)
		storeNode(ret)
		return ret
	}

	protected def finishOperator(CallOperationAction expr, String symbol, Operation operator, Action left,
		Action right) {
		val returnType = operator.ownedParameters.findFirst[name == "return"].type
		expr.operation = operator
		expr.createResult("result", returnType)
		left.result.objectFlow(expr.createArgument("left", left.result.type))
		right.result.objectFlow(expr.createArgument("right", right.result.type))
		expr.name = '''«left.name»«symbol»«right.name»'''
	}
}