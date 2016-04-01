package hu.elte.txtuml.export.uml2.restructured.activity.statement

import hu.elte.txtuml.export.uml2.restructured.Exporter
import org.eclipse.jdt.core.dom.ReturnStatement
import org.eclipse.uml2.uml.SequenceNode

class ReturnStatementExporter extends ControlExporter<ReturnStatement, SequenceNode> {
	
	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(ReturnStatement access) { factory.createSequenceNode }
	
	override exportContents(ReturnStatement source) {
		val expr = exportExpression(source.expression)[storeNode]
		result.name = "return " + expr.name
		expr.result.objectFlow(getParameterNode("return"))
	}
	
}