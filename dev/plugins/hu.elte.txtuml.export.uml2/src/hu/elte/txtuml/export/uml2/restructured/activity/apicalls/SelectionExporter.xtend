package hu.elte.txtuml.export.uml2.restructured.activity.apicalls

import hu.elte.txtuml.export.uml2.restructured.activity.ActionExporter
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.uml2.uml.CallOperationAction
import hu.elte.txtuml.export.uml2.restructured.BaseExporter

class SelectionExporter extends ActionExporter<MethodInvocation, CallOperationAction> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(MethodInvocation access) {
		if (isApiMethodInvocation(access.resolveMethodBinding) && access.resolveMethodBinding.name == "selectAny")
			factory.createCallOperationAction
	}

	override exportContents(MethodInvocation source) {
		val expr = source.expression.exportExpression
//		expr.objectFlow(result.inputs.get(0))
		result.name = "selectAny"
		result.createResult(result.name, null)
	}

}