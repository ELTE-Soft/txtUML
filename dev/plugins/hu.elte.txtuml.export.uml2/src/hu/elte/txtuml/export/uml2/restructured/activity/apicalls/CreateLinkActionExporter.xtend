package hu.elte.txtuml.export.uml2.restructured.activity.apicalls

import hu.elte.txtuml.export.uml2.restructured.activity.apicalls.LinkActionExporterBase
import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.TypeLiteral

class CreateLinkActionExporter extends LinkActionExporterBase<org.eclipse.uml2.uml.CreateLinkAction> {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(MethodInvocation access) {
		if (isApiMethodInvocation(access.resolveMethodBinding) && access.resolveMethodBinding.name == "link")
			factory.createCreateLinkAction
	}
	
	override exportContents(MethodInvocation source) {
		val leftLit = source.arguments.get(0) as TypeLiteral
		val leftEnd = createEnd(factory.createLinkEndCreationData, leftLit.type.resolveBinding, source.arguments.get(1) as Expression)
		val rightLit = source.arguments.get(2) as TypeLiteral
		val rightEnd = createEnd(factory.createLinkEndCreationData, rightLit.type.resolveBinding, source.arguments.get(3) as Expression)
		result.endData += #[leftEnd, rightEnd]
		result.name = '''link «leftEnd.value.name» to «rightEnd.value.name»'''
	}
	
	
}