package hu.elte.txtuml.export.uml2.restructured.activity.apicalls

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.export.uml2.restructured.activity.ActionExporter
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.uml2.uml.SendObjectAction

class SendActionExporter extends ActionExporter<MethodInvocation, SendObjectAction> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(MethodInvocation access) {
		if (isApiMethodInvocation(access.resolveMethodBinding) && access.resolveMethodBinding.name == "send")
			factory.createSendObjectAction
	}

	override exportContents(MethodInvocation source) {
		val signalArg = source.arguments.get(0) as Expression
		val signalToSend = signalArg.exportExpression
		signalToSend.objectFlow(result.createRequest("signal_to_send", fetchType(signalArg.resolveTypeBinding)))

		val targetArg = source.arguments.get(1) as Expression
		val target = targetArg.exportExpression
		target.objectFlow(result.createTarget("receiver", fetchType(targetArg.resolveTypeBinding)))
		result.name = '''send «signalToSend.name» to «target.name»'''
	}

}