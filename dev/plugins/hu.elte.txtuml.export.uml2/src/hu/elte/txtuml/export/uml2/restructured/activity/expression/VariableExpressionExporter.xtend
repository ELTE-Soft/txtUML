package hu.elte.txtuml.export.uml2.restructured.activity.expression

import hu.elte.txtuml.export.uml2.restructured.activity.ActionExporter
import org.eclipse.uml2.uml.ReadVariableAction
import org.eclipse.jdt.core.dom.Name
import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import org.eclipse.jdt.core.dom.IVariableBinding

class VariableExpressionExporter extends ActionExporter<Name, ReadVariableAction> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(Name access) {
		val binding = access.resolveBinding
		if (binding instanceof IVariableBinding && !(binding as IVariableBinding).isField)
			factory.createReadVariableAction
	}

	override exportContents(Name source) {
		result.name = source.resolveBinding.name
		result.createResult(result.name, fetchType(source.resolveTypeBinding))
	}
}