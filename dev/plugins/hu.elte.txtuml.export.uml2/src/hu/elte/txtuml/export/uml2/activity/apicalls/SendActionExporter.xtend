package hu.elte.txtuml.export.uml2.activity.apicalls

import hu.elte.txtuml.export.uml2.BaseExporter
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.jdt.core.dom.ExpressionMethodReference

class SendActionExporter extends SendActionExporterBase {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(MethodInvocation access) {
		if (isApiMethodInvocation(access.resolveMethodBinding) && access.resolveMethodBinding.name == "send" &&
			!(access.arguments.get(1) instanceof ExpressionMethodReference)
		)
			factory.createSendObjectAction
	}

	override exportContents(MethodInvocation source) {
		val signalToSend = exportSignal(source)

		val targetArg = source.arguments.get(1) as Expression
		val target = targetArg.exportExpression
		target.objectFlow(result.createTarget("receiver", fetchType(targetArg.resolveTypeBinding)))
		result.name = '''send «signalToSend.name» to «target.name»'''
	}

}