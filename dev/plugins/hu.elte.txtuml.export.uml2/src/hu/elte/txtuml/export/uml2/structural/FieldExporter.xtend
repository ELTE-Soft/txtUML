package hu.elte.txtuml.export.uml2.structural

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.Exporter
import org.eclipse.jdt.core.dom.IVariableBinding
import org.eclipse.uml2.uml.NamedElement
import org.eclipse.uml2.uml.Property
import org.eclipse.uml2.uml.TypedElement
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import hu.elte.txtuml.export.uml2.utils.MultiplicityProvider
import org.eclipse.uml2.uml.MultiplicityElement

abstract class TypedNamedMultipliedExporter<T extends TypedElement & NamedElement & MultiplicityElement> extends Exporter<IVariableBinding, IVariableBinding, T> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent);
	}

	override exportContents(IVariableBinding binding) {
		result.name = binding.name
		if(ElementTypeTeller.isCollection(binding.type)) {
			result.upper = MultiplicityProvider.getUpperBound(binding.type)
			result.lower = MultiplicityProvider.getLowerBound(binding.type)
			result.type = fetchType(binding.type.typeArguments.get(0))
		} else {
			result.type = fetchType(binding.type)		
		}
		result.visibility = getVisibility(binding.getModifiers)
	}
}

class FieldExporter extends TypedNamedMultipliedExporter<Property> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(IVariableBinding source) {
		if(source.isField) factory.createProperty
	}
}
