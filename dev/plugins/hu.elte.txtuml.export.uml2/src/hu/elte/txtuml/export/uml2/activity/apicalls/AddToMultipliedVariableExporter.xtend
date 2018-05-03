package hu.elte.txtuml.export.uml2.activity.apicalls

import hu.elte.txtuml.export.uml2.activity.ActionExporter
import org.eclipse.jdt.core.dom.MethodInvocation
import hu.elte.txtuml.export.uml2.BaseExporter
import org.eclipse.uml2.uml.AddVariableValueAction

class AddToMultipliedVariableExporter extends ActionExporter<MethodInvocation, AddVariableValueAction> {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}	
	
	override create(MethodInvocation access) {
		if (isApiMethodInvocation(access.resolveMethodBinding) && 
			access.resolveMethodBinding.name == "add"
		) {
			factory.createAddVariableValueAction
		}
	
	}
	
	override exportContents(MethodInvocation source) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
}