package hu.elte.txtuml.export.uml2.restructured.activity.statement

import hu.elte.txtuml.export.uml2.restructured.Exporter
import org.eclipse.jdt.core.dom.ReturnStatement
import org.eclipse.uml2.uml.SequenceNode
import hu.elte.txtuml.export.uml2.restructured.activity.expression.assign.AssignToVariableExporter

class ReturnStatementExporter extends ControlExporter<ReturnStatement, SequenceNode> {

	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(ReturnStatement access) { factory.createSequenceNode }

	override exportContents(ReturnStatement source) {
		val ret = exportExpression(source.expression)
		new AssignToVariableExporter(this).createWriteVariableAction(getVariable("return"), ret)
		result.name = "return " + ret.name
	}

}