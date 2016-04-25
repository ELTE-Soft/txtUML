package hu.elte.txtuml.export.uml2.restructured.activity.statement

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import org.eclipse.jdt.core.dom.Block
import org.eclipse.uml2.uml.SequenceNode

class BlockExporter extends ControlExporter<Block, SequenceNode> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(Block access) { factory.createSequenceNode }

	override exportContents(Block source) {
		result.executableNodes += source.statements.map[exportStatement]
	}
}