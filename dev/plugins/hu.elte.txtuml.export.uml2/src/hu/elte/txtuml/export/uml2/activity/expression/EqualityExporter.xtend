package hu.elte.txtuml.export.uml2.activity.expression

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.activity.ActionExporter
import org.eclipse.jdt.core.dom.InfixExpression
import org.eclipse.jdt.core.dom.InfixExpression.Operator
import org.eclipse.uml2.uml.Action
import org.eclipse.uml2.uml.TestIdentityAction

class EqualityExporter extends ActionExporter<InfixExpression, TestIdentityAction> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(InfixExpression access) {
		if(access.operator == Operator.EQUALS) factory.createTestIdentityAction
	}

	override exportContents(InfixExpression source) {
		val left = exportExpression(source.leftOperand)
		val right = exportExpression(source.rightOperand)
		finishIdentityTest(result, left, right)
	}

	def exportOperator(Action left, Action right) {
		val ret = factory.createTestIdentityAction
		finishIdentityTest(ret, left, right)
		storeNode(ret)
		return ret
	}

	protected def finishIdentityTest(TestIdentityAction expr, Action left, Action right) {
		expr.createResult("result", booleanType)
		left.result.objectFlow(expr.createFirst("left", left.result.type))
		right.result.objectFlow(expr.createSecond("right", right.result.type))
		expr.name = '''«left.name»==«right.name»'''
	}
}