package hu.elte.txtuml.export.uml2.restructured.activity.apicalls

import hu.elte.txtuml.export.uml2.restructured.Exporter
import hu.elte.txtuml.export.uml2.restructured.activity.ActionExporter
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.uml2.uml.CallOperationAction

class LogActionExporter extends ActionExporter<MethodInvocation, CallOperationAction> {

	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(MethodInvocation access) {
		if (API_CLASSES.contains(access.resolveMethodBinding.declaringClass.qualifiedName) &&
			access.name.identifier == "log")
			factory.createCallOperationAction
	}

	override exportContents(MethodInvocation source) {
		val arg = exportExpression(source.arguments.get(0) as Expression)
		arg.result.objectFlow(result.createArgument("msg", stringType))
		result.operation = getImportedOperation("StringOperations", "log");
		result.name = source.toString
	}

}