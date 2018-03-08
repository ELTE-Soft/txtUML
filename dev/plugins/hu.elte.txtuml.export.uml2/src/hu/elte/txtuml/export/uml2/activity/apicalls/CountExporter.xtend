package hu.elte.txtuml.export.uml2.activity.apicalls

import hu.elte.txtuml.export.uml2.activity.ActionExporter
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.uml2.uml.CallOperationAction
import hu.elte.txtuml.export.uml2.BaseExporter

class CountExporter extends ActionExporter<MethodInvocation, CallOperationAction> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(MethodInvocation access) {
		if (isApiMethodInvocation(access.resolveMethodBinding) && access.resolveMethodBinding.name == "size")
			factory.createCallOperationAction
	}

	override exportContents(MethodInvocation source) {
		val expr = source.expression.exportExpression
		expr.objectFlow(result.createArgument("counted", expr.result.type))
		result.operation = getImportedOperation("ObjectOperations", "count")
		result.name = '''count(«expr.name»)'''
		result.createResult(result.name, integerType)
	}

}