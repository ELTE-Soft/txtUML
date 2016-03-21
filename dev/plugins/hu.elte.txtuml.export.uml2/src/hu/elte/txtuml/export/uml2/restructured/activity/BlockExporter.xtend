package hu.elte.txtuml.export.uml2.restructured.activity

import hu.elte.txtuml.export.uml2.restructured.Exporter
import org.eclipse.jdt.core.dom.Block
import org.eclipse.uml2.uml.Element
import org.eclipse.uml2.uml.ExecutableNode
import org.eclipse.uml2.uml.SequenceNode

class BlockExporter extends Exporter<Block, Block, SequenceNode> {

	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(Block access) {
		switch access {
			Block: factory.createSequenceNode
		}
	}

	override exportContents(Block source) {
		source.statements.forEach[exportStatement]
	}

	override tryStore(Element contained) {
		switch contained {
			ExecutableNode: result.executableNodes.add(contained)
			default: return false
		}
		return true
	}

}