package hu.elte.txtuml.export.uml2.activity

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.activity.statement.ControlExporter
import org.eclipse.jdt.core.dom.Block
import org.eclipse.uml2.uml.ActivityParameterNode
import org.eclipse.uml2.uml.ParameterDirectionKind
import org.eclipse.uml2.uml.SequenceNode

class FinishBlockExporter extends ControlExporter<Block, SequenceNode> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(Block access) { factory.createSequenceNode }

	override exportContents(Block source) {
		result.name = "#finish"
		result.activity.ownedNodes.filter[it instanceof ActivityParameterNode].map [
			it as ActivityParameterNode
		].filter [
			parameter.direction == ParameterDirectionKind.OUT_LITERAL ||
				parameter.direction == ParameterDirectionKind.RETURN_LITERAL
		].forEach [
			val paramVar = getVariable(name)
			paramVar.read.result.objectFlow(it)
		]
	}

}