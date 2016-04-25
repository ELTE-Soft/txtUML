package hu.elte.txtuml.export.uml2.activity

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.Exporter
import hu.elte.txtuml.export.uml2.structural.OperationExporter
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