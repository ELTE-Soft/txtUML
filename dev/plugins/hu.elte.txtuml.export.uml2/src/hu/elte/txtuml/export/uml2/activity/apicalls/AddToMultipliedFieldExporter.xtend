package hu.elte.txtuml.export.uml2.activity.apicalls

import hu.elte.txtuml.export.uml2.activity.ActionExporter
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.uml2.uml.AddStructuralFeatureValueAction
import hu.elte.txtuml.export.uml2.BaseExporter

class AddToMultipliedFieldExporter extends ActionExporter<MethodInvocation, AddStructuralFeatureValueAction> {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(MethodInvocation access) {
				if (isApiMethodInvocation(access.resolveMethodBinding) && 
			access.resolveMethodBinding.name == "add"
		) {
			factory.createAddStructuralFeatureValueAction
		}
	}
	
	override exportContents(MethodInvocation source) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
}