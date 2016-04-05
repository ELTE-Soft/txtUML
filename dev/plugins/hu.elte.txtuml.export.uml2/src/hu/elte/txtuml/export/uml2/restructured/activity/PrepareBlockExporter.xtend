package hu.elte.txtuml.export.uml2.restructured.activity

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import org.eclipse.jdt.core.dom.Block
import org.eclipse.uml2.uml.SequenceNode
import org.eclipse.uml2.uml.ParameterDirectionKind

class PrepareBlockExporter extends ActionExporter<Block, SequenceNode> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(Block access) { factory.createSequenceNode }

	override exportContents(Block source) {
		(parent as ActivityExporter).result.ownedParameters.forEach [
			val paramNode = factory.createActivityParameterNode
			paramNode.parameter = it
			paramNode.name = name
			paramNode.type = type
			storeNode(paramNode)

			val paramVar = factory.createVariable
			paramVar.name = name
			paramVar.type = type
			storeVariable(paramVar)

			if (direction == ParameterDirectionKind.IN_LITERAL || direction == ParameterDirectionKind.INOUT_LITERAL) {
				// here we cannot use the standard VariableExpressionExporter function, because ActivityParameterBlock is not an Action
				val initAssign = factory.createAddVariableValueAction
				initAssign.name = '''fetch(«name»)'''
				initAssign.isReplaceAll = true
				initAssign.variable = paramVar
				paramNode.objectFlow(initAssign.createValue("new_value", type))
				storeNode(initAssign)
			}
		]
	}

}