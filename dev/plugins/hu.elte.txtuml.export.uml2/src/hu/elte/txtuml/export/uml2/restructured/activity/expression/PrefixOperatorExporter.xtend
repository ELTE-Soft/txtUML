package hu.elte.txtuml.export.uml2.restructured.activity.expression

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.export.uml2.restructured.activity.ActionExporter
import org.eclipse.jdt.core.dom.PrefixExpression
import org.eclipse.jdt.core.dom.PrefixExpression.Operator
import org.eclipse.uml2.uml.CallOperationAction

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
		val returnType = result.operation.ownedParameters.findFirst[name == "return"].type
		val op = exportExpression(source.operand)
		op.result.objectFlow(result.inputs.get(0))
		result.operation = operator
		result.createResult("result", returnType)
		result.name = '''«source.operator»«op.name»'''
	}
}