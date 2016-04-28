package hu.elte.txtuml.export.uml2.activity.statement

import hu.elte.txtuml.export.uml2.activity.statement.ControlExporter
import org.eclipse.jdt.core.dom.EmptyStatement
import org.eclipse.uml2.uml.SequenceNode
import hu.elte.txtuml.export.uml2.BaseExporter

class EmptyStmtExporter extends ControlExporter<EmptyStatement, SequenceNode> {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(EmptyStatement access) { factory.createSequenceNode }
	
	override exportContents(EmptyStatement source) {
		result.name = ";"
	}
	
}