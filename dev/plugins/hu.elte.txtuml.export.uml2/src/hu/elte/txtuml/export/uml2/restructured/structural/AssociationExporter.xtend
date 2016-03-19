package hu.elte.txtuml.export.uml2.restructured.structural

import hu.elte.txtuml.export.uml2.restructured.Exporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.uml2.uml.Association
import org.eclipse.uml2.uml.Element
import org.eclipse.uml2.uml.Property

class AssociationExporter extends Exporter<TypeDeclaration, ITypeBinding, Association> {

	new(Exporter<?, ?, ?> exporter) {
		super(exporter)
	}
	
	override create(ITypeBinding access) {
		if(ElementTypeTeller.isAssociation(access)) factory.createAssociation
	}

	override exportContents(TypeDeclaration decl) {
		result.name = decl.name.identifier
		decl.types.forEach[exportAssociationEnd]
	}

	override tryStore(Element contained) {
		switch contained {
			Property: result.ownedEnds.add(contained)
			default: return false
		}
		return true
	}

}