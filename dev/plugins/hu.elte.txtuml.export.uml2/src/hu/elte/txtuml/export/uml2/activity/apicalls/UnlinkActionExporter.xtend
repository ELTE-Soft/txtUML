package hu.elte.txtuml.export.uml2.activity.apicalls

import hu.elte.txtuml.export.uml2.BaseExporter
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.jdt.core.dom.TypeLiteral
import org.eclipse.uml2.uml.DestroyLinkAction

class UnlinkActionExporter extends LinkActionExporterBase<DestroyLinkAction> {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(MethodInvocation access) {
		if (isApiMethodInvocation(access.resolveMethodBinding) && access.resolveMethodBinding.name == "unlink")
			factory.createDestroyLinkAction
	}
	
	override exportContents(MethodInvocation source) {
		val leftLit = source.arguments.get(0) as TypeLiteral
		val leftEnd = createEnd(factory.createLinkEndDestructionData, leftLit.type.resolveBinding, source.arguments.get(1) as Expression)
		val rightLit = source.arguments.get(2) as TypeLiteral
		val rightEnd = createEnd(factory.createLinkEndDestructionData, rightLit.type.resolveBinding, source.arguments.get(3) as Expression)
		result.endData += #[leftEnd, rightEnd]
		result.name = '''unlink «leftEnd.value.name» from «rightEnd.value.name»'''
	}
	
	
}