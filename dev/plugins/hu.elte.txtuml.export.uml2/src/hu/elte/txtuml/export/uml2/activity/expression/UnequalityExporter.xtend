package hu.elte.txtuml.export.uml2.activity.expression

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.activity.ActionExporter
import org.eclipse.jdt.core.dom.InfixExpression
import org.eclipse.jdt.core.dom.InfixExpression.Operator
import org.eclipse.uml2.uml.CallOperationAction

class UnequalityExporter extends ActionExporter<InfixExpression, CallOperationAction> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(InfixExpression access) { if(access.operator == Operator.NOT_EQUALS) factory.createCallOperationAction }

	override exportContents(InfixExpression source) {
		val left = exportExpression(source.leftOperand)
		val right = exportExpression(source.rightOperand)
		val eqTest = new EqualityExporter(this).exportOperator(left, right)
		new PrefixOperatorExporter(this).finishLogicalNot(result, eqTest)
	}
}