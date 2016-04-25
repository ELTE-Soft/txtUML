package hu.elte.txtuml.export.uml2.restructured.activity.apicalls

import hu.elte.txtuml.export.uml2.restructured.activity.ActionExporter
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.uml2.uml.CallOperationAction
import hu.elte.txtuml.export.uml2.restructured.BaseExporter

class GetSignalExporter extends ActionExporter<MethodInvocation, CallOperationAction> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(MethodInvocation access) {
		if (isApiMethodInvocation(access.resolveMethodBinding) && access.resolveMethodBinding.name == "getSignal")
			factory.createCallOperationAction
	}

	override exportContents(MethodInvocation source) {
		source.resolveTypeBinding
		result.operation = getImportedOperation("SignalOperations", "getSignal")
		result.name = "getSignal()"
		result.createResult(result.name, fetchType(source.resolveTypeBinding))
	}

}