package hu.elte.txtuml.export.uml2.restructured.activity.expression

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.export.uml2.restructured.activity.ActionExporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import org.eclipse.jdt.core.dom.Name
import org.eclipse.uml2.uml.ReadVariableAction

class VariableExpressionExporter extends ActionExporter<Name, ReadVariableAction> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(Name access) {
		if (ElementTypeTeller.isVariable(access)) factory.createReadVariableAction
	}

	override exportContents(Name source) {
		result.name = source.resolveBinding.name
		result.createResult(result.name, fetchType(source.resolveTypeBinding))
	}
}