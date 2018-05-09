package hu.elte.txtuml.export.uml2.activity.statement

import hu.elte.txtuml.export.uml2.Exporter
import org.eclipse.jdt.core.dom.ReturnStatement
import org.eclipse.uml2.uml.SequenceNode

class ReturnStatementExporter extends ControlExporter<ReturnStatement, SequenceNode> {

	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(ReturnStatement access) { factory.createSequenceNode }

	override exportContents(ReturnStatement source) {
		val ret = exportExpression(source.expression)
		getVariable("return").write(ret, true)
		result.name = "return " + ret.name
	}

}