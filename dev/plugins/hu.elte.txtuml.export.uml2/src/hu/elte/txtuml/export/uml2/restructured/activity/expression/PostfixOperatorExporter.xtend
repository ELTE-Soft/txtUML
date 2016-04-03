package hu.elte.txtuml.export.uml2.restructured.activity.expression

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.export.uml2.restructured.activity.ActionExporter
import org.eclipse.jdt.core.dom.PostfixExpression
import org.eclipse.jdt.core.dom.PostfixExpression.Operator
import org.eclipse.uml2.uml.CallOperationAction

class PostfixOperatorExporter extends ActionExporter<PostfixExpression, CallOperationAction> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(PostfixExpression access) {
		factory.createCallOperationAction
	}

	override exportContents(PostfixExpression source) {
		val operator = switch source.operator {
			case Operator.INCREMENT:
				getImportedOperation("IntegerOperations", "delayedInc")
			case Operator.DECREMENT:
				getImportedOperation("IntegerOperations", "delayedDec")
		}
		val returnType = result.operation.ownedParameters.findFirst[name == "return"].type
		val op = exportExpression(source.operand)
		op.result.objectFlow(result.inputs.get(0))
		result.operation = operator
		result.createResult("result", returnType)
		result.name = '''«op.name»«source.operator»'''
	}
}