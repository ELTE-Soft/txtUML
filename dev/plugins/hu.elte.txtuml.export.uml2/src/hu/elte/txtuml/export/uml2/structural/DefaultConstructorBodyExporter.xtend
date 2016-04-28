package hu.elte.txtuml.export.uml2.structural

import hu.elte.txtuml.export.uml2.Exporter
import org.eclipse.uml2.uml.Activity
import org.eclipse.jdt.core.dom.IMethodBinding
import hu.elte.txtuml.export.uml2.BaseExporter

class DefaultConstructorBodyExporter extends Exporter<IMethodBinding, IMethodBinding, Activity> {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(IMethodBinding access) { if (access.isDefaultConstructor) factory.createActivity }
	
	override exportContents(IMethodBinding source) {
		result.specification = fetchElement(source, new DefaultConstructorExporter(this))
		result.name = result.specification.name
	}

}