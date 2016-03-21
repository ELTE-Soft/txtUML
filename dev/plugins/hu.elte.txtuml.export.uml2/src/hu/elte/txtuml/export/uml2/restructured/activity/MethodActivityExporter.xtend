package hu.elte.txtuml.export.uml2.restructured.activity

import hu.elte.txtuml.export.uml2.restructured.Exporter
import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.jdt.core.dom.SingleVariableDeclaration
import org.eclipse.uml2.uml.Activity
import org.eclipse.uml2.uml.Element
import org.eclipse.uml2.uml.Parameter
import org.eclipse.uml2.uml.ParameterDirectionKind
import org.eclipse.uml2.uml.ActivityNode
import org.eclipse.uml2.uml.UMLPackage
import org.eclipse.uml2.uml.ActivityEdge
import org.eclipse.uml2.uml.Variable

class MethodActivityExporter extends Exporter<MethodDeclaration, IMethodBinding, Activity> {

	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(IMethodBinding access) { factory.createActivity }

	override exportContents(MethodDeclaration decl) {
		val binding = decl.resolveBinding
		result.name = binding.name
		if (binding.returnType.qualifiedName != void.canonicalName) {
			val retParam = factory.createParameter
			retParam.type = fetchType(binding.returnType)
			retParam.direction = ParameterDirectionKind.RETURN_LITERAL
			result.ownedParameters.add(retParam)
		}
		decl.parameters.forEach[exportParameter((it as SingleVariableDeclaration).resolveBinding)]
		val init = result.createOwnedNode(binding.name + "_init", UMLPackage.Literals.INITIAL_NODE);
		val final = result.createOwnedNode(binding.name + "_final", UMLPackage.Literals.FINAL_NODE);
		controlFlow(init, result.nodes.get(0))
		controlFlow(result.nodes.last, final)
	}

	override tryStore(Element contained) {
		switch contained {
			Parameter: result.ownedParameters.add(contained)
			Variable: result.variables.add(contained)
			ActivityNode: result.nodes.add(contained)
			ActivityEdge: result.edges.add(contained)
			default: return false
		}
		return true
	}

	def controlFlow(ActivityNode from, ActivityNode to) {
		val edge = result.createEdge("control_flow_from_" + from.name + "_to_" + to.name,
			UMLPackage.Literals.CONTROL_FLOW);
		edge.source = from
		edge.target = to
	}

}