package hu.elte.txtuml.export.uml2.structural

import hu.elte.txtuml.api.model.AssociationEnd.Navigable
import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.Exporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import hu.elte.txtuml.utils.jdt.SharedUtils
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.uml2.uml.Association
import org.eclipse.uml2.uml.Class

class AssociationExporter extends Exporter<TypeDeclaration, ITypeBinding, Association> {

	new(BaseExporter<?, ?, ?> exporter) {
		super(exporter)
	}

	override create(ITypeBinding access) {
		if(ElementTypeTeller.isAssociation(access)) factory.createAssociation
	}

	override exportContents(TypeDeclaration decl) {
		result.name = decl.name.identifier
		val classes = decl.types.map[fetchType(resolveBinding.superclass.typeArguments.get(0)) as Class]

		decl.types.forEach [ td, i |
			exportAssociationEnd(td) [
				if (SharedUtils.typeIsAssignableFrom(td, Navigable)) {
					classes.get(1 - i).ownedAttributes += it
				} else {
					result.ownedEnds += it
				}
			]
		]

	}
}