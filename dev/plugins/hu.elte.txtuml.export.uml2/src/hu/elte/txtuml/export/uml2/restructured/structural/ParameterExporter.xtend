package hu.elte.txtuml.export.uml2.restructured.structural

import hu.elte.txtuml.export.uml2.restructured.Exporter
import org.eclipse.jdt.core.dom.IVariableBinding
import org.eclipse.uml2.uml.Parameter
import org.eclipse.uml2.uml.ParameterDirectionKind

class ParameterExporter extends TypedNamedExporter<Parameter> {

	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(IVariableBinding source) {
		if(source.isParameter) factory.createParameter
	}
	
	override exportContents(IVariableBinding binding) {
		super.exportContents(binding)
		result.direction = ParameterDirectionKind.IN_LITERAL
	}

}