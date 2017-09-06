package hu.elte.txtuml.export.uml2.activity.apicalls

import hu.elte.txtuml.export.uml2.activity.ActionExporter
import hu.elte.txtuml.export.uml2.BaseExporter
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.uml2.uml.SequenceNode

class ConnectExporter extends ActionExporter<MethodInvocation, SequenceNode>{
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(MethodInvocation access) {
		if (isApiMethodInvocation(access.resolveMethodBinding) 
			&& access.resolveMethodBinding.name == "connect"
		) {
			factory.createSequenceNode
		}
	}
	
	override exportContents(MethodInvocation source) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
}