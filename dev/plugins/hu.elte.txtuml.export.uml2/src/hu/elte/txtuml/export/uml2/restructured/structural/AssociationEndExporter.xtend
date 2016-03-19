package hu.elte.txtuml.export.uml2.restructured.structural

import hu.elte.txtuml.api.model.assocends.Navigability.Navigable
import hu.elte.txtuml.export.uml2.utils.MultiplicityProvider
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import hu.elte.txtuml.utils.jdt.SharedUtils
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.uml2.uml.AggregationKind
import org.eclipse.uml2.uml.Element
import org.eclipse.uml2.uml.Property

class AssociationEndExporter extends hu.elte.txtuml.export.uml2.restructured.Exporter<TypeDeclaration, ITypeBinding, Property> {

	new(hu.elte.txtuml.export.uml2.restructured.Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(ITypeBinding a) {
		if(ElementTypeTeller.isAssociationEnd(a)) factory.createProperty
	}

	override exportContents(TypeDeclaration decl) {
		result.name = decl.name.identifier
		result.type = fetchType(decl.resolveBinding.superclass.typeArguments.get(0))
		result.lower = MultiplicityProvider.getLowerBound(decl);
		result.upper = MultiplicityProvider.getUpperBound(decl);

		result.association = fetchElement((decl.parent as TypeDeclaration).resolveBinding)
		result.isNavigable = SharedUtils.typeIsAssignableFrom(decl, Navigable)
		result.aggregation = if (ElementTypeTeller.isComposition(decl.getParent() as TypeDeclaration)) 
			AggregationKind.COMPOSITE_LITERAL
		else
			AggregationKind.NONE_LITERAL
		
	}

	override tryStore(Element contained) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

}