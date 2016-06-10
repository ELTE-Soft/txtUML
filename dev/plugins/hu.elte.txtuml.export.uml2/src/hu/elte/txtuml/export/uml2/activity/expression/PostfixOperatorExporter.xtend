package hu.elte.txtuml.export.uml2.activity.expression

import hu.elte.txtuml.export.uml2.BaseExporter
import org.eclipse.jdt.core.dom.PostfixExpression
import org.eclipse.jdt.core.dom.PostfixExpression.Operator

class PostfixOperatorExporter extends OperatorExporter<PostfixExpression> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(PostfixExpression access) {
		factory.createSequenceNode
	}

	override exportContents(PostfixExpression source) {
		val operator = switch source.operator {
			case Operator.INCREMENT:
				getImportedOperation("IntegerOperations", "add")
			case Operator.DECREMENT:
				getImportedOperation("IntegerOperations", "sub")
		}
		assignToExpressionDelayed(source.operand) [ act |
			val callOp = factory.createCallOperationAction
			callOp.operation = operator
			val lhs = act.get
			lhs.objectFlow(callOp.createArgument("arg", integerType))
			val litOne = new NumberLiteralExporter(this).createIntegerLiteral(1)
			litOne.objectFlow(callOp.createArgument("inc", integerType))
			callOp.name = '''«lhs.name»«source.operator»'''
			result.name = callOp.name
			callOp.createResult(callOp.name, integerType)
			storeNode(callOp)
			return callOp
		]
	}
}