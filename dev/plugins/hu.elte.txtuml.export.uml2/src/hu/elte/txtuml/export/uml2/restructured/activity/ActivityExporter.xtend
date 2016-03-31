package hu.elte.txtuml.export.uml2.restructured.activity

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import org.eclipse.jdt.core.dom.Block
import org.eclipse.uml2.uml.Activity
import org.eclipse.uml2.uml.ActivityEdge
import org.eclipse.uml2.uml.ActivityNode
import org.eclipse.uml2.uml.UMLPackage
import org.eclipse.uml2.uml.Variable

class ActivityExporter extends ActionExporter<Block, Activity> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(Block access) { factory.createActivity }

	override exportContents(Block block) {
		val init = result.createOwnedNode("#init", UMLPackage.Literals.INITIAL_NODE)
		val body = block.statements.map[exportStatement]
		result.ownedNodes += body
		val final = result.createOwnedNode("#final", UMLPackage.Literals.ACTIVITY_FINAL_NODE)
		if (body.isEmpty) {
			init.controlFlow(final)
		} else {
			init.controlFlow(body.get(0))
			body.last.controlFlow(final)
		}
	}

	override storeEdge(ActivityEdge edge) { result.edges += edge }

	override storeNode(ActivityNode node) { result.nodes += node }

	override storeVariable(Variable variable) { result.variables += variable }

}