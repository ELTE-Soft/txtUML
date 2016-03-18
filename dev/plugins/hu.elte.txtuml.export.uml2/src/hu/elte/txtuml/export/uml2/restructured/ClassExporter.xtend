package hu.elte.txtuml.export.uml2.restructured

import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.uml2.uml.Class
import org.eclipse.uml2.uml.Element
import org.eclipse.uml2.uml.Operation
import org.eclipse.uml2.uml.Property

class ClassExporter extends Exporter<TypeDeclaration, Class> {
	
	new(Exporter<?,?> parent) {
		super(parent)
	}
	
	override create() { factory.createClass }
	
	override exportContents(TypeDeclaration typeDecl) {
		result.isAbstract = ElementTypeTeller.isAbstract(typeDecl)
		result.name = typeDecl.name.identifier
//		typeDecl.fields.forEach[ attributeExporter.export ]
	}
	
	override tryStore(Element contained) {
		switch contained {
			Operation: result.ownedOperations.add(contained)
			Property: result.ownedAttributes.add(contained)
			default: return false
		}
		return true;
	}
	
}