package hu.elte.txtuml.export.uml2.activity.apicalls

import hu.elte.txtuml.export.uml2.BaseExporter
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.uml2.uml.ReadStructuralFeatureAction
import org.eclipse.uml2.uml.Port
import org.eclipse.jdt.core.dom.ExpressionMethodReference

class SendToPortActionExporter extends SendActionExporterBase {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(MethodInvocation access) {
		if (isApiMethodInvocation(access.resolveMethodBinding) && access.resolveMethodBinding.name == "send" && 
			access.arguments.get(1) instanceof ExpressionMethodReference
		)
			factory.createSendObjectAction
	}
	
	override exportContents(MethodInvocation source) {
		val signalToSend = exportSignal(source)

		val targetArg = source.arguments.get(1) as ExpressionMethodReference
		val target = targetArg.exportExpression as ReadStructuralFeatureAction
		val readAction = target as ReadStructuralFeatureAction;
		val port = readAction.structuralFeature as Port;
		target.objectFlow(result.createTarget("receiver", port.type))
		result.name = '''send «signalToSend.name» to «target.name»'''
	}
	
	
}