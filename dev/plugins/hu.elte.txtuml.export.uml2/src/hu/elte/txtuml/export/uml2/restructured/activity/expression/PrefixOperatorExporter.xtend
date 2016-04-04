package hu.elte.txtuml.export.uml2.restructured.activity.expression

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.export.uml2.restructured.activity.ActionExporter
import org.eclipse.jdt.core.dom.PrefixExpression
import org.eclipse.jdt.core.dom.PrefixExpression.Operator
import org.eclipse.uml2.uml.CallOperationAction
import org.eclipse.uml2.uml.Operation
import org.eclipse.uml2.uml.Action

class PrefixOperatorExporter extends ActionExporter<PrefixExpression, CallOperationAction> {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(PrefixExpression access) {
		if(access.operator != Operator.PLUS) factory.createCallOperationAction
	}

	override exportContents(PrefixExpression source) {
		val operator = switch source.operator {
			case Operator.MINUS:
				getImportedOperation("IntegerOperations", "neg")
			case Operator.INCREMENT:
				getImportedOperation("IntegerOperations", "inc")
			case Operator.DECREMENT:
				getImportedOperation("IntegerOperations", "dec")
			case Operator.NOT:
				getImportedOperation("BooleanOperations", "not")
		}
		val op = exportExpression(source.operand)
		result.name = '''«source.operator»«op.name»'''
		finishOperation(op, operator)
	}
	
	def logicalNot(Action expr) {
		result = factory.createCallOperationAction
		val operator = getImportedOperation("BooleanOperations", "not")
		result.name = '''!«expr.name»'''
		finishOperation(expr, operator)
		result
	}
	
	protected def finishOperation(Action operand, Operation operator) {
		result.operation = operator
		val returnType = result.operation.ownedParameters.findFirst[name == "return"].type
		operand.result.objectFlow(result.createArgument("b", operand.result.type))
		result.createResult("result", returnType)
	}
}