package hu.elte.txtuml.export.uml2.activity.apicalls

import hu.elte.txtuml.export.uml2.BaseExporter
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.uml2.uml.ReadStructuralFeatureAction
import org.eclipse.uml2.uml.Port
import org.eclipse.jdt.core.dom.ExpressionMethodReference
import org.eclipse.jdt.core.dom.FieldAccess

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
		
		val methodRefernce =  source.arguments.get(1) as ExpressionMethodReference
		val fieldAcces = methodRefernce.expression as FieldAccess
		val methodInv = fieldAcces.expression as MethodInvocation
		val target = methodInv.exportExpression as ReadStructuralFeatureAction
		val readAction = target as ReadStructuralFeatureAction;
		val port = readAction.structuralFeature as Port;
		target.objectFlow(result.createTarget("receiver", port.type))
		result.name = '''send «signalToSend.name» to «target.name»'''
	}
	
	
}