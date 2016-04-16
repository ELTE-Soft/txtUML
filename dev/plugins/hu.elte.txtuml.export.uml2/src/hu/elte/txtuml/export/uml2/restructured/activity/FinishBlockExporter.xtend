package hu.elte.txtuml.export.uml2.restructured.activity

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.export.uml2.restructured.activity.expression.VariableExpressionExporter
import hu.elte.txtuml.export.uml2.restructured.activity.statement.ControlExporter
import org.eclipse.jdt.core.dom.Block
import org.eclipse.uml2.uml.ActivityParameterNode
import org.eclipse.uml2.uml.SequenceNode
import org.eclipse.uml2.uml.ParameterDirectionKind

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
			val read = new VariableExpressionExporter(this).readVar(paramVar)
			read.result.objectFlow(it)
		]
	}

}