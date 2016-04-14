package hu.elte.txtuml.export.uml2.restructured.structural

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.export.uml2.restructured.Exporter
import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.jdt.core.dom.SingleVariableDeclaration
import org.eclipse.uml2.uml.Operation
import org.eclipse.uml2.uml.ParameterDirectionKind
import org.eclipse.uml2.uml.Stereotype
import org.eclipse.jdt.core.dom.Modifier

class OperationExporter extends Exporter<MethodDeclaration, IMethodBinding, Operation> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(IMethodBinding s) {
		factory.createOperation
	}

	override exportContents(MethodDeclaration decl) {
		val binding = decl.resolveBinding
		result.name = binding.name
		if (binding.returnType.qualifiedName != void.canonicalName || decl.isConstructor) {
			val retParam = factory.createParameter
			retParam.type = fetchType(if (decl.isConstructor) decl.resolveBinding.declaringClass else binding.returnType)
			retParam.direction = ParameterDirectionKind.RETURN_LITERAL
			result.ownedParameters += retParam
		}
		result.methods += fetchElement(decl.resolveBinding, new MethodActivityExporter(this))
		result.ownedParameters += decl.parameters.map [
			exportParameter((it as SingleVariableDeclaration).resolveBinding)
		]
		result.isStatic = Modifier.isStatic(decl.getModifiers)
		if (decl.isConstructor) {
			result.applyStereotype(getImportedElement("Create") as Stereotype)
		}
	}

}