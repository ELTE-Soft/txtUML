package hu.elte.txtuml.export.uml2.activity

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.Exporter
import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.uml2.uml.Activity
import org.eclipse.jdt.core.dom.SingleVariableDeclaration
import org.eclipse.uml2.uml.ParameterDirectionKind

class SMActivityExporter extends Exporter<MethodDeclaration, IMethodBinding, Activity> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(IMethodBinding access) { factory.createActivity }

	override exportContents(MethodDeclaration decl) {
		val binding = decl.resolveBinding
		result.name = binding.name
		if (decl.resolveBinding.returnType.name != "void") {
			val retParam = result.createOwnedParameter("return", fetchType(decl.resolveBinding.returnType))
			retParam.direction = ParameterDirectionKind.RETURN_LITERAL
		}
		decl.parameters.forEach[ SingleVariableDeclaration arg |
			result.createOwnedParameter(arg.name.identifier, fetchType(arg.type.resolveBinding))
		]
		decl.body?.exportActivity(result)
	}

}