package hu.elte.txtuml.export.uml2.structural

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.Exporter
import org.eclipse.jdt.core.dom.IVariableBinding
import org.eclipse.uml2.uml.NamedElement
import org.eclipse.uml2.uml.Property
import org.eclipse.uml2.uml.TypedElement
import org.eclipse.uml2.uml.MultiplicityElement

abstract class TypedNamedMultipliedExporter<T extends TypedElement & NamedElement & MultiplicityElement> extends Exporter<IVariableBinding, IVariableBinding, T> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent);
	}

	override exportContents(IVariableBinding binding) {
		result.name = binding.name
		fillElementTypeAndBounds(binding.type, result)
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
