package hu.elte.txtuml.export.uml2.activity.expression

import hu.elte.txtuml.export.uml2.activity.ActionExporter
import org.eclipse.uml2.uml.Action
import org.eclipse.jdt.core.dom.PrefixExpression
import hu.elte.txtuml.export.uml2.BaseExporter
import org.eclipse.jdt.core.dom.PrefixExpression.Operator

class PrefixPlusExporter extends ActionExporter<PrefixExpression, Action> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(PrefixExpression access) {
		if(access.operator == Operator.PLUS) exportExpression(access.operand)
	}

	override exportContents(PrefixExpression source) {
	}

}