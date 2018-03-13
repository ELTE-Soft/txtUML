package hu.elte.txtuml.export.uml2.structural

import hu.elte.txtuml.api.model.AssociationEnd.Navigable
import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.Exporter
import hu.elte.txtuml.export.uml2.utils.MultiplicityProvider
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import hu.elte.txtuml.utils.jdt.SharedUtils
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.uml2.uml.AggregationKind
import org.eclipse.uml2.uml.Property

class AssociationEndExporter extends Exporter<TypeDeclaration, ITypeBinding, Property> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(ITypeBinding a) {
		if(ElementTypeTeller.isAssociationEnd(a)) factory.createProperty
	}

	override exportContents(TypeDeclaration decl) {
		result.name = decl.name.identifier
		val collectionType = decl.resolveBinding.superclass.superclass.typeArguments.get(0)
		result.type = fetchType(collectionType.typeArguments.get(0))
		result.lower = MultiplicityProvider.getLowerBound(collectionType);
		result.upper = MultiplicityProvider.getUpperBound(collectionType);

		result.association = (parent as AssociationExporter).result
		result.isNavigable = SharedUtils.typeIsAssignableFrom(decl, Navigable)
		result.aggregation = if (ElementTypeTeller.isContained(decl)) {
			AggregationKind.COMPOSITE_LITERAL
		} else {
			AggregationKind.NONE_LITERAL
		}
	}
}
