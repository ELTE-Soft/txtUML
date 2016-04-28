package hu.elte.txtuml.export.uml2.activity

import hu.elte.txtuml.export.uml2.BaseExporter
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
		// TODO: in case of ctor if the first statement is not super() or this() insert call
		// to the implicit super ctor
		val init = result.createOwnedNode("#init", UMLPackage.Literals.INITIAL_NODE)
		// for activities of operation, the parameters are in the operation, 
		// for activities in a state machine, the parameters are in the activity
		(result.specification?.ownedParameters ?: result.ownedParameters)?.forEach[
			result.createVariable(name, type)
		]
		val prepare = exportPreparetBlock(block)
		val body = block.exportBlock[storeNode]
		body.name = "#body"
		val finishBlock = exportFinishBlock(block)
		
		val final = result.createOwnedNode("#final", UMLPackage.Literals.ACTIVITY_FINAL_NODE)

		init.controlFlow(prepare)
		prepare.controlFlow(body)
		body.controlFlow(finishBlock)
		finishBlock.controlFlow(final)
	}

	override storeEdge(ActivityEdge edge) { result.edges += edge }

	override storeNode(ActivityNode node) { result.ownedNodes += node }

	override storeVariable(Variable variable) { result.variables += variable }

	override getVariable(String varName) {
		result.variables.findFirst[varName == name] ?: super.getVariable(varName)
	}
	
	def exportPreparetBlock(Block blk) { cache.export(new PrepareBlockExporter(this), blk, blk, [storeNode]) }
	
	def exportFinishBlock(Block blk) { cache.export(new FinishBlockExporter(this), blk, blk, [storeNode]) }
}