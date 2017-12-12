package hu.elte.txtuml.export.uml2.activity.apicalls

import org.eclipse.jdt.core.dom.MethodInvocation
import hu.elte.txtuml.export.uml2.BaseExporter
import org.eclipse.jdt.core.dom.TypeLiteral

class DelegationConnectExporter extends ConnectExporterBase {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(MethodInvocation access) {
		if (access.arguments.size == 3) {
			resultCreate(access)
		}
	}
	
	override exportContents(MethodInvocation source) {
		val parentPortReference = source.arguments.get(0) as MethodInvocation
		val parentTypeLit = source.arguments.get(1) as TypeLiteral
		val parentTypeBinding = parentTypeLit.type.resolveBinding
		
		val childPortReference = source.arguments.get(2) as MethodInvocation
		createAddToPortAction(parentPortReference,childPortReference);
		createAddToPortAction(childPortReference,parentPortReference);
	}
	
}