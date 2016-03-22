package hu.elte.txtuml.export.uml2.restructured.activity

import hu.elte.txtuml.export.uml2.restructured.Exporter
import org.eclipse.jdt.core.dom.Block
import org.eclipse.uml2.uml.Activity
import org.eclipse.uml2.uml.ActivityEdge
import org.eclipse.uml2.uml.UMLPackage
import org.eclipse.uml2.uml.ActivityNode
import org.eclipse.uml2.uml.Variable

class ActivityExporter extends ActionExporter<Block, Activity> {

	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(Block access) { factory.createActivity }

	override exportContents(Block block) {
		val init = result.createOwnedNode("_init", UMLPackage.Literals.INITIAL_NODE)
		val final = result.createOwnedNode("_final", UMLPackage.Literals.ACTIVITY_FINAL_NODE)
		val exportedBlock = exportBlock(block)
		result.nodes.add(exportedBlock)
		init.controlFlow(exportedBlock)
		exportedBlock.controlFlow(final)
	}

	override storeEdge(ActivityEdge edge) { result.edges += edge }

	override storeNode(ActivityNode node) { result.nodes += node }
	
	override storeVariable(Variable variable) { result.variables += variable }

}