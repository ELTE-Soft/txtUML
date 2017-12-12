package hu.elte.txtuml.export.uml2.activity.apicalls

import org.eclipse.jdt.core.dom.MethodInvocation
import hu.elte.txtuml.export.uml2.BaseExporter
import org.eclipse.jdt.core.dom.TypeLiteral

class AssemblyConnectExporter extends ConnectExporterBase {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(MethodInvocation access) {
		if (access.arguments.size == 4) {
			resultCreate(access)
		}
	}
	
	override exportContents(MethodInvocation source) {
		val p1 = source.arguments.get(1) as MethodInvocation
		val p2 = source.arguments.get(3) as MethodInvocation
		
		val leftLit = source.arguments.get(0) as TypeLiteral
		val rigthLit = source.arguments.get(2) as TypeLiteral
		val leftEnd = createEnd(factory.createLinkEndCreationData, leftLit.type.resolveBinding, p1)
		val rightEnd = createEnd(factory.createLinkEndCreationData, rigthLit.type.resolveBinding, p2)
		
		result.endData += #[leftEnd, rightEnd]
		result.name = '''link «leftEnd.value.name» to «rightEnd.value.name»'''
		
	}
	
}