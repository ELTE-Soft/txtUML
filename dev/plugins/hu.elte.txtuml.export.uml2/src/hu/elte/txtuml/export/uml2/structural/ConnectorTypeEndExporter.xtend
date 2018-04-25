package hu.elte.txtuml.export.uml2.structural

import hu.elte.txtuml.export.uml2.Exporter
import hu.elte.txtuml.export.uml2.BaseExporter
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.uml2.uml.Property
import hu.elte.txtuml.export.uml2.utils.MultiplicityProvider
import org.eclipse.uml2.uml.Interface
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import hu.elte.txtuml.api.model.Interface.Empty

class ConnectorTypeEndExporter extends Exporter<TypeDeclaration, ITypeBinding, Property> {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(ITypeBinding access) {
		factory.createProperty
	}
	
	override exportContents(TypeDeclaration source) {
		result.name = source.name.identifier		
		var portTypeBinging = source.resolveBinding.superclass.typeArguments.get(1)

		var providedTypeArgument = portTypeBinging.superclass.typeArguments.get(0)		
		val providedInterfaceBase = fetchType(providedTypeArgument)	as Interface	
		if(ElementTypeTeller.isOutPort(portTypeBinging)) {
			result.type = getImportedElement(Empty.canonicalName) as Interface
		} else {
			result.type = providedInterfaceBase
		}
		val assocEndType = source.resolveBinding.superclass.typeArguments.get(0)
		val collectionType = assocEndType.superclass.typeArguments.get(0)
		
		result.lower = MultiplicityProvider.getLowerBound(collectionType);
		result.upper = MultiplicityProvider.getUpperBound(collectionType);
		result.association = (parent as ConnectorTypeExporter).result
		result.isNavigable = true
		
	}
	
}