package hu.elte.txtuml.export.uml2.restructured.activity.expression

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.export.uml2.restructured.activity.ActionExporter
import org.eclipse.jdt.core.dom.IVariableBinding
import org.eclipse.jdt.core.dom.Name
import org.eclipse.uml2.uml.ActivityParameterNode

/** The nodes for the parameters must be created in the activity. This exporter just accesses them. */
class ParameterExpressionExporter extends ActionExporter<Name, ActivityParameterNode> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(Name access) {
		val binding = access.resolveBinding
		if (binding instanceof IVariableBinding && (binding as IVariableBinding).isParameter)
			getParameterNode(binding.name)
	}

	override exportContents(Name source) {}
}