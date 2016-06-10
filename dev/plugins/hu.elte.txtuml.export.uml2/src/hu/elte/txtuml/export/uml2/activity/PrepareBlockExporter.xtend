package hu.elte.txtuml.export.uml2.activity

import hu.elte.txtuml.export.uml2.BaseExporter
import org.eclipse.jdt.core.dom.Block
import org.eclipse.uml2.uml.SequenceNode
import org.eclipse.uml2.uml.ParameterDirectionKind
import hu.elte.txtuml.export.uml2.activity.statement.ControlExporter

class PrepareBlockExporter extends ControlExporter<Block, SequenceNode> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(Block access) { factory.createSequenceNode }

	override exportContents(Block source) {
		(result.activity.specification?.ownedParameters ?: result.activity.ownedParameters)?.forEach [
			val paramNode = factory.createActivityParameterNode
			paramNode.parameter = it
			paramNode.name = name
			paramNode.type = type
			storeNode(paramNode)

			if (direction == ParameterDirectionKind.IN_LITERAL || direction == ParameterDirectionKind.INOUT_LITERAL) {
				// here we cannot use the standard VariableExpressionExporter function, because ActivityParameterBlock is not an Action
				val initAssign = factory.createAddVariableValueAction
				initAssign.name = '''fetch(«name»)'''
				initAssign.isReplaceAll = true
				initAssign.variable = getVariable(name)
				paramNode.objectFlow(initAssign.createValue("new_value", type))
				storeNode(initAssign)
			}
		]
	}

}