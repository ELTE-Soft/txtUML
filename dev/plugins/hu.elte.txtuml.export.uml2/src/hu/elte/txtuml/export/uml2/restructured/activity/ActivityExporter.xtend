package hu.elte.txtuml.export.uml2.restructured.activity

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import org.eclipse.jdt.core.dom.Block
import org.eclipse.uml2.uml.Activity
import org.eclipse.uml2.uml.ActivityEdge
import org.eclipse.uml2.uml.ActivityNode
import org.eclipse.uml2.uml.SequenceNode
import org.eclipse.uml2.uml.UMLPackage
import org.eclipse.uml2.uml.Variable
import org.eclipse.uml2.uml.ActivityParameterNode

class ActivityExporter extends ActionExporter<Block, Activity> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(Block access) { factory.createActivity }

	override exportContents(Block block) {
		result.ownedParameters.forEach [
			val paramNode = factory.createActivityParameterNode
			paramNode.parameter = it
			paramNode.name = name + "_node"
			paramNode.type = type
			storeNode(paramNode)
		]
		val init = result.createOwnedNode("#init", UMLPackage.Literals.INITIAL_NODE)
		val body = result.createOwnedNode("#body", UMLPackage.Literals.SEQUENCE_NODE) as SequenceNode
		block.statements.forEach[exportStatement[body.executableNodes += it]]
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