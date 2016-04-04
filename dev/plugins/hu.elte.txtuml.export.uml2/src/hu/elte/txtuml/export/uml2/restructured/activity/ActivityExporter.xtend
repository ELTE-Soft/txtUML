package hu.elte.txtuml.export.uml2.restructured.activity

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import org.eclipse.jdt.core.dom.Block
import org.eclipse.uml2.uml.Activity
import org.eclipse.uml2.uml.ActivityEdge
import org.eclipse.uml2.uml.ActivityNode
import org.eclipse.uml2.uml.ActivityParameterNode
import org.eclipse.uml2.uml.UMLPackage
import org.eclipse.uml2.uml.Variable
import org.eclipse.uml2.uml.SequenceNode
import org.eclipse.uml2.uml.AddVariableValueAction

class ActivityExporter extends ActionExporter<Block, Activity> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(Block access) { factory.createActivity }

	override exportContents(Block block) {
		val init = result.createOwnedNode("#init", UMLPackage.Literals.INITIAL_NODE)
		val prepare = result.createOwnedNode("#prepare", UMLPackage.Literals.SEQUENCE_NODE) as SequenceNode
		result.ownedParameters.forEach [
			val paramNode = factory.createActivityParameterNode
			paramNode.parameter = it
			paramNode.name = name + "_node"
			paramNode.type = type
			storeNode(paramNode)
			
			val paramVar = result.createVariable(name, type)
			val initAssign = prepare.createExecutableNode("", UMLPackage.Literals.ADD_VARIABLE_VALUE_ACTION) as AddVariableValueAction
			initAssign.isReplaceAll = true
			initAssign.variable = paramVar
			paramNode.objectFlow(initAssign.createValue("", type))
		]
		val body = block.exportBlock[storeNode]
		val final = result.createOwnedNode("#final", UMLPackage.Literals.ACTIVITY_FINAL_NODE)
		if (body.executableNodes.empty) {
			init.controlFlow(final)
		} else {
			init.controlFlow(body)
			body.controlFlow(final)
		}
	}

	override storeEdge(ActivityEdge edge) { result.edges += edge }

	override storeNode(ActivityNode node) { result.ownedNodes += node }

	override storeVariable(Variable variable) { result.variables += variable }

	override getParameterNode(String name) {
		result.ownedNodes.findFirst[it.name == name + "_node"] as ActivityParameterNode
	}

}