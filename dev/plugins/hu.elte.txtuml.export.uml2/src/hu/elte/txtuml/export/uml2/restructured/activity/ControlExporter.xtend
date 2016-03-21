package hu.elte.txtuml.export.uml2.restructured.activity

import hu.elte.txtuml.export.uml2.restructured.Exporter
import org.eclipse.uml2.uml.Action
import org.eclipse.uml2.uml.ActivityEdge
import org.eclipse.uml2.uml.ActivityNode
import org.eclipse.uml2.uml.Element
import org.eclipse.uml2.uml.ExecutableNode
import org.eclipse.uml2.uml.StructuredActivityNode
import org.eclipse.uml2.uml.Type
import org.eclipse.uml2.uml.UMLPackage
import org.eclipse.uml2.uml.Variable
import org.eclipse.uml2.uml.WriteVariableAction

abstract class ControlExporter<S, R extends StructuredActivityNode> extends Exporter<S, S, R> {

	override tryStore(Element contained) {
		switch contained {
			Variable: result.variables.add(contained)
			ActivityNode: result.nodes.add(contained)
			ActivityEdge: result.edges.add(contained)
			default: return false
		}
		return true
	}

	def writeVariable(Variable variable, ExecutableNode testExpr) {
		val writevar = result.createNode("write_" + testExpr.name + "_to_" + variable.name,
			UMLPackage.Literals.WRITE_VARIABLE_ACTION) as WriteVariableAction
		writevar.createValue(testExpr.name, testExpr.type)
		writevar.variable = variable
		return writevar
	}

	def dispatch Type getType(Action node) {
		node.outputs.get(0).type
	}

	def dispatch Type getType(ExecutableNode node) {
		throw new RuntimeException("Cannot type " + node.toString)
	}

}