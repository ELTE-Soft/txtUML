package hu.elte.txtuml.export.uml2.structural

import hu.elte.txtuml.api.model.ModelClass
import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.Exporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.uml2.uml.Classifier
import org.eclipse.uml2.uml.Interface

class InterfaceExporter extends Exporter<TypeDeclaration, ITypeBinding, Interface> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(ITypeBinding typ) {
		if(ElementTypeTeller.isInterface(typ)) factory.createInterface
	}

	override exportContents(TypeDeclaration typeDecl) {
		val typeBnd = typeDecl.resolveBinding
		typeBnd.declaredFields.forEach[exportField[result.ownedAttributes += it]]
		typeDecl.methods.forEach[exportOperation[result.ownedOperations += it]]
		if (typeDecl.superclassType != null &&
			typeDecl.superclassType.resolveBinding.qualifiedName != ModelClass.canonicalName) {
			result.createGeneralization(fetchType(typeDecl.superclassType.resolveBinding) as Classifier)
		}
		result.name = typeBnd.name
	}

}