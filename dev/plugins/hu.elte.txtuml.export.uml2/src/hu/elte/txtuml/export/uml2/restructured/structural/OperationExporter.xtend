package hu.elte.txtuml.export.uml2.restructured.structural

import hu.elte.txtuml.export.uml2.restructured.Exporter
import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.uml2.uml.Element
import org.eclipse.uml2.uml.Operation
import org.eclipse.uml2.uml.Parameter
import org.eclipse.uml2.uml.ParameterDirectionKind
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.jdt.core.dom.SingleVariableDeclaration

class OperationExporter extends Exporter<MethodDeclaration, IMethodBinding, Operation> {

	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(IMethodBinding s) {
		factory.createOperation
	}

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
	}

	override tryStore(Element contained) {
		switch contained {
			Parameter: result.ownedParameters.add(contained)
			default: return false
		}
		return true
	}

}