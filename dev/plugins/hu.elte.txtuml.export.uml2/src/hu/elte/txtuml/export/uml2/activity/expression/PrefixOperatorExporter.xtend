package hu.elte.txtuml.export.uml2.activity.expression

import hu.elte.txtuml.export.uml2.BaseExporter
import org.eclipse.jdt.core.dom.PrefixExpression
import org.eclipse.jdt.core.dom.PrefixExpression.Operator
import org.eclipse.uml2.uml.Action
import org.eclipse.uml2.uml.CallOperationAction
import org.eclipse.uml2.uml.Operation

class PrefixOperatorExporter extends OperatorExporter<PrefixExpression> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(PrefixExpression access) {
		if(access.operator != Operator.PLUS) factory.createSequenceNode
	}

	override exportContents(PrefixExpression source) {
		val operator = switch source.operator {
			case Operator.MINUS:
				getImportedOperation("IntegerOperations", "neg")
			case Operator.INCREMENT:
				getImportedOperation("IntegerOperations", "add")
			case Operator.DECREMENT:
				getImportedOperation("IntegerOperations", "sub")
			case Operator.NOT:
				getImportedOperation("BooleanOperations", "not")
		}
		switch source.operator {
			case Operator.MINUS,
			case Operator.NOT: {
				val act = factory.createCallOperationAction
				val op = exportExpression(source.operand)
				result.name = '''«source.operator»«op.name»'''
				finishOperation(act, op, operator)
				storeNode(act)
			}
			case Operator.INCREMENT,
			case Operator.DECREMENT: {
				assignToExpression(source.operand) [ act |
					val callOp = factory.createCallOperationAction
					callOp.operation = operator
					val lhs = act.get
					lhs.objectFlow(callOp.createArgument("arg", integerType))
					val litOne = new NumberLiteralExporter(this).createIntegerLiteral(1)
					litOne.objectFlow(callOp.createArgument("change", integerType))
					callOp.name = '''«source.operator»«lhs.name»'''
					result.name = callOp.name
					callOp.createResult(callOp.name, integerType)
					storeNode(callOp)
					return callOp
				]
			}
		}

	}

	def logicalNot(Action expr) {
		val act = factory.createCallOperationAction
		finishLogicalNot(act, expr)
	}
	
	def finishLogicalNot(CallOperationAction act, Action expr) {
		val operator = getImportedOperation("BooleanOperations", "not")
		act.name = '''!«expr.name»'''
		finishOperation(act, expr, operator)
		return act
	}

	protected def finishOperation(CallOperationAction act, Action operand, Operation operator) {
		act.operation = operator
		val returnType = act.operation.ownedParameters.findFirst[name == "return"].type
		operand.result.objectFlow(act.createArgument("b", operand.result.type))
		act.createResult("result", returnType)
	}
}