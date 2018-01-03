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
		val childPortReference = source.arguments.get(2) as MethodInvocation
		
		val leftLit = source.arguments.get(0) as TypeLiteral
		val rigthLit = source.arguments.get(2) as TypeLiteral
		val leftEnd = createEnd(factory.createLinkEndCreationData, leftLit.type.resolveBinding, parentPortReference)
		val rightEnd = createEnd(factory.createLinkEndCreationData, rigthLit.type.resolveBinding, childPortReference)
		
		result.endData += #[leftEnd, rightEnd]
		result.name = '''link «leftEnd.value.name» to «rightEnd.value.name»'''
	}
	
}