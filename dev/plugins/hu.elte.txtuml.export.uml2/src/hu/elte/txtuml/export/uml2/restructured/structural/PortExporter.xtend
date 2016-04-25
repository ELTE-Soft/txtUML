package hu.elte.txtuml.export.uml2.restructured.structural

import hu.elte.txtuml.export.uml2.restructured.Exporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.uml2.uml.Interface
import org.eclipse.uml2.uml.Port
import hu.elte.txtuml.export.uml2.restructured.BaseExporter

class PortExporter extends Exporter<TypeDeclaration, ITypeBinding, Port> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(ITypeBinding access) { if(ElementTypeTeller.isPort(access)) factory.createPort }

	override exportContents(TypeDeclaration source) {
		val typeArguments = source.resolveBinding.superclass.typeArguments
		val required = fetchType(typeArguments.get(0)) as Interface
		val provided = fetchType(typeArguments.get(1)) as Interface
		if (ElementTypeTeller.isBehavioralPort(source)) {
			result.isBehavior = true;
		}
		val dummyProvided = factory.createInterface
		val dummyInherit = factory.createGeneralization
		dummyInherit.specific = dummyProvided
		dummyInherit.general = provided
		val providedRequired = factory.createUsage
		providedRequired.clients += dummyProvided
		providedRequired.suppliers += required
		result.type = dummyProvided
		
		storePackaged(providedRequired)
		storePackaged(dummyProvided)
		
		dummyProvided.name = '''«provided.name» for «source.name.identifier»'''
		result.name = source.name.identifier
	}

}