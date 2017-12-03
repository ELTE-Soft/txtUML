package hu.elte.txtuml.export.uml2.structural

import hu.elte.txtuml.export.uml2.Exporter
import hu.elte.txtuml.export.uml2.BaseExporter
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.uml2.uml.Property
import hu.elte.txtuml.export.uml2.utils.MultiplicityProvider
import org.eclipse.uml2.uml.Port

class ConnectorTypeEndExporter extends Exporter<TypeDeclaration, ITypeBinding, Property> {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(ITypeBinding access) {
		factory.createProperty
	}
	
	override exportContents(TypeDeclaration source) {
		result.name = source.name.identifier
		//TODO export port interface properly
		var superType = source.superclassType
		
		var portTypeBinging = source.resolveBinding.superclass.typeArguments.get(1)
		var providedTypeArgument = portTypeBinging.typeArguments.get(0)
		//val decl = portTypeBinging.parent
		//val portElement = exportPort(source.resolveBinding.superclass.typeArguments.get(1))[]
		//result.type = portElement.type
		
		result.lower = MultiplicityProvider.getLowerBound(source);
		result.upper = MultiplicityProvider.getUpperBound(source);
		result.association = (parent as ConnectorTypeExporter).result
		result.isNavigable = true
		
	}
	
}