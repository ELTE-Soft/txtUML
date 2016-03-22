package hu.elte.txtuml.export.uml2.restructured.activity

import hu.elte.txtuml.export.uml2.restructured.Exporter
import org.eclipse.jdt.core.dom.ExpressionStatement
import org.eclipse.uml2.uml.SequenceNode

class ExpressionStatementExporter extends ControlExporter<ExpressionStatement, SequenceNode> {
	
	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(ExpressionStatement access) {
		factory.createSequenceNode
	}
	
	override exportContents(ExpressionStatement source) {
		exportExpression(source.expression)
	}
	
}