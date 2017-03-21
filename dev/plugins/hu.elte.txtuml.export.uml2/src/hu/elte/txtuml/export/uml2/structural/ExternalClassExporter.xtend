package hu.elte.txtuml.export.uml2.structural

import hu.elte.txtuml.export.uml2.Exporter
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.TypeDeclaration
//import hu.elte.txtuml.api.model.external.ExternalClass
import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import org.eclipse.uml2.uml.Interface
//import org.eclipse.uml2.uml.Classifier
//import hu.elte.txtuml.api.model.ModelClass

class ExternalClassExporter extends Exporter<TypeDeclaration, ITypeBinding, Interface>  {
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(ITypeBinding typ) {
		if(ElementTypeTeller.isExternalClass(typ)) factory.createInterface
	}
	
	override exportContents(TypeDeclaration typeDecl) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
		/*val typeBnd = typeDecl.resolveBinding
		typeBnd.declaredFields.forEach[exportField[result.ownedAttributes += it]]
		typeDecl.methods.forEach[exportOperation[result.ownedOperations += it]]
		if (typeDecl.superclassType != null &&
			typeDecl.superclassType.resolveBinding.qualifiedName != ModelClass.canonicalName) {
			result.createGeneralization(fetchType(typeDecl.superclassType.resolveBinding) as Classifier)
		}
		result.name = typeBnd.name*/
	}
	
}