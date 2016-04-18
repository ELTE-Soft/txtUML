package hu.elte.txtuml.export.uml2.restructured.activity.statement

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.export.uml2.restructured.activity.ActionExporter
import org.eclipse.uml2.uml.Action
import org.eclipse.uml2.uml.ActivityEdge
import org.eclipse.uml2.uml.ActivityNode
import org.eclipse.uml2.uml.ExecutableNode
import org.eclipse.uml2.uml.StructuredActivityNode
import org.eclipse.uml2.uml.Type
import org.eclipse.uml2.uml.Variable

abstract class ControlExporter<S, R extends StructuredActivityNode> extends ActionExporter<S, R> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	def dispatch Type getType(Action node) {
		node.outputs.get(0).type
	}

	def dispatch Type getType(ExecutableNode node) {
		throw new RuntimeException("Cannot type " + node.toString)
	}

	override storeEdge(ActivityEdge edge) { if (result != null) result.edges += edge else super.storeEdge(edge) }

	override storeNode(ActivityNode node) { if (result != null && node instanceof ExecutableNode) result.nodes += node else super.storeNode(node) }

	override storeVariable(Variable variable) { if (result != null) result.variables += variable else super.storeVariable(variable) }

	override getVariable(String varName) { 
		result.variables.findFirst[name == varName] ?: super.getVariable(varName)
	}

}