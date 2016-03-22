package hu.elte.txtuml.export.uml2.restructured.structural

import hu.elte.txtuml.export.uml2.restructured.Exporter
import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.jdt.core.dom.SingleVariableDeclaration
import org.eclipse.uml2.uml.Operation
import org.eclipse.uml2.uml.ParameterDirectionKind

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
		result.ownedParameters.addAll(decl.parameters.map [
			exportParameter((it as SingleVariableDeclaration).resolveBinding)
		])
	}

}