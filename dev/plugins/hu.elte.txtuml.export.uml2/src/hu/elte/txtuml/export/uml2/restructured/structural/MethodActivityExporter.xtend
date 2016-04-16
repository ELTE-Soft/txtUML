package hu.elte.txtuml.export.uml2.restructured.structural

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.export.uml2.restructured.Exporter
import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.uml2.uml.Activity

class MethodActivityExporter extends Exporter<MethodDeclaration, IMethodBinding, Activity> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(IMethodBinding access) { factory.createActivity }

	override exportContents(MethodDeclaration decl) {
		val binding = decl.resolveBinding
		result.name = binding.name
		result.specification = fetchElement(binding, new OperationExporter(this))
		decl.body?.exportActivity(result)
	}

}