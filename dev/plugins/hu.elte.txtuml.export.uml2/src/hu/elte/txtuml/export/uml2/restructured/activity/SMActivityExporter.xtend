package hu.elte.txtuml.export.uml2.restructured.activity

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.export.uml2.restructured.Exporter
import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.uml2.uml.Activity
import org.eclipse.jdt.core.dom.SingleVariableDeclaration

class SMActivityExporter extends Exporter<MethodDeclaration, IMethodBinding, Activity> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(IMethodBinding access) { factory.createActivity }

	override exportContents(MethodDeclaration decl) {
		val binding = decl.resolveBinding
		result.name = binding.name
		if (decl.resolveBinding.returnType.name != "void") {
			result.createOwnedParameter("return", fetchType(decl.resolveBinding.returnType))
		}
		decl.parameters.forEach[ SingleVariableDeclaration arg |
			result.createOwnedParameter(arg.name.identifier, fetchType(arg.type.resolveBinding))
		]
		decl.body?.exportActivity(result)
	}

}