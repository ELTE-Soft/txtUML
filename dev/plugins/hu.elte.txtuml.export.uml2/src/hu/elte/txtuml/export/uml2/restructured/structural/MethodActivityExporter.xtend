package hu.elte.txtuml.export.uml2.restructured.structural

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.export.uml2.restructured.Exporter
import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.jdt.core.dom.SingleVariableDeclaration
import org.eclipse.uml2.uml.Activity
import org.eclipse.uml2.uml.ActivityNode
import org.eclipse.uml2.uml.ParameterDirectionKind
import org.eclipse.uml2.uml.UMLPackage

class MethodActivityExporter extends Exporter<MethodDeclaration, IMethodBinding, Activity> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(IMethodBinding access) { factory.createActivity }

	override exportContents(MethodDeclaration decl) {
		val binding = decl.resolveBinding
		result.name = binding.name
		handleReturnParameter(binding)
		result.ownedParameters.addAll(decl.parameters.map [
			exportParameter((it as SingleVariableDeclaration).resolveBinding)
		])
		exportActivity(decl.body, result)
	}
	
	def handleReturnParameter(IMethodBinding binding) {
		if (binding.returnType.qualifiedName != void.canonicalName) {
			val retParam = factory.createParameter
			retParam.type = fetchType(binding.returnType)
			retParam.direction = ParameterDirectionKind.RETURN_LITERAL
			result.ownedParameters.add(retParam)
		}
	}

	def controlFlow(ActivityNode from, ActivityNode to) {
		val edge = result.createEdge("control_flow_from_" + from.name + "_to_" + to.name,
			UMLPackage.Literals.CONTROL_FLOW);
		edge.source = from
		edge.target = to
	}

}