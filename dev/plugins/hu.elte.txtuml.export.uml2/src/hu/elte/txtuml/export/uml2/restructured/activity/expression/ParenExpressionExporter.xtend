package hu.elte.txtuml.export.uml2.restructured.activity.expression

import hu.elte.txtuml.export.uml2.restructured.activity.ActionExporter
import org.eclipse.jdt.core.dom.ParenthesizedExpression
import org.eclipse.uml2.uml.Action
import hu.elte.txtuml.export.uml2.restructured.BaseExporter

class ParenExpressionExporter extends ActionExporter<ParenthesizedExpression, Action> {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(ParenthesizedExpression access) {
		exportExpression(access.expression)
	}
	
	override exportContents(ParenthesizedExpression source) {
	}
	
}