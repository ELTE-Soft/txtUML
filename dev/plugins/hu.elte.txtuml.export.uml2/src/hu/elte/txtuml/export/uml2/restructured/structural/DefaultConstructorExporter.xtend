package hu.elte.txtuml.export.uml2.restructured.structural

import hu.elte.txtuml.export.uml2.restructured.Exporter
import org.eclipse.uml2.uml.Operation
import org.eclipse.jdt.core.dom.IMethodBinding
import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import org.eclipse.uml2.uml.Stereotype

class DefaultConstructorExporter extends Exporter<IMethodBinding, IMethodBinding, Operation> {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(IMethodBinding access) { if (access.isDefaultConstructor) factory.createOperation }
	
	override exportContents(IMethodBinding source) {
		val createStereotype = getImportedElement("Create") as Stereotype
		result.applyStereotype(createStereotype)
		result.name = source.name
	}
	
}