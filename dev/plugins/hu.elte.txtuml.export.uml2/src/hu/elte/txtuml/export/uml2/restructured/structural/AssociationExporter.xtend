package hu.elte.txtuml.export.uml2.restructured.structural

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.export.uml2.restructured.Exporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.uml2.uml.Association

class AssociationExporter extends Exporter<TypeDeclaration, ITypeBinding, Association> {

	new(BaseExporter<?, ?, ?> exporter) {
		super(exporter)
	}

	override create(ITypeBinding access) {
		if(ElementTypeTeller.isAssociation(access)) factory.createAssociation
	}

	override exportContents(TypeDeclaration decl) {
		result.name = decl.name.identifier
		result.ownedEnds.addAll(decl.types.map[exportAssociationEnd])
	}
}