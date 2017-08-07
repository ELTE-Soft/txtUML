package hu.elte.txtuml.export.uml2.activity.apicalls

import hu.elte.txtuml.export.uml2.activity.ActionExporter
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.uml2.uml.SendObjectAction
import hu.elte.txtuml.export.uml2.BaseExporter
import org.eclipse.jdt.core.dom.Expression

abstract class SendActionExporterBase extends ActionExporter<MethodInvocation, SendObjectAction>{
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	def exportSignal(MethodInvocation source) {
		val signalArg = source.arguments.get(0) as Expression
		val signalToSend = signalArg.exportExpression
		signalToSend.objectFlow(result.createRequest("signal_to_send", fetchType(signalArg.resolveTypeBinding)))
		signalToSend
	}

	
}