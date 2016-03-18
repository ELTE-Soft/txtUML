package hu.elte.txtuml.export.uml2.restructured.structural

import hu.elte.txtuml.export.uml2.restructured.Exporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.uml2.uml.Class
import org.eclipse.uml2.uml.Element
import org.eclipse.uml2.uml.Operation
import org.eclipse.uml2.uml.Property
import org.eclipse.jdt.core.dom.TypeDeclaration

class ClassExporter extends Exporter<TypeDeclaration, ITypeBinding, Class> {

	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(TypeDeclaration typ) { createFetched(typ.resolveBinding) }
	
	override createFetched(ITypeBinding typ) { 
		if (ElementTypeTeller.isModelClass(typ)) { factory.createClass }
	}

	override exportContents(TypeDeclaration typeDecl) {
		val typeBnd = typeDecl.resolveBinding
		result.isAbstract = ElementTypeTeller.isAbstract(typeBnd)
		result.name = typeBnd.name
		typeBnd.declaredFields.forEach[exportField]
		typeDecl.methods.forEach[exportOperation]
	}

	override tryStore(Element contained) {
		switch contained {
			Operation: result.ownedOperations.add(contained)
			Property: result.ownedAttributes.add(contained)
			default: return false
		}
		return true
	}

}