package hu.elte.txtuml.export.uml2.activity.apicalls

import hu.elte.txtuml.export.uml2.BaseExporter
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.jdt.core.dom.TypeLiteral
import org.eclipse.uml2.uml.ReadLinkAction

class ReadLinkActionExporter extends LinkActionExporterBase<ReadLinkAction> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(MethodInvocation access) {
		if (isApiMethodInvocation(access.resolveMethodBinding) && access.resolveMethodBinding.name == "assoc")
			factory.createReadLinkAction
	}

	override exportContents(MethodInvocation source) {
		val clsLit = source.arguments.get(0) as TypeLiteral
		val otherEndType = clsLit.type.resolveBinding
		val thisEndType = otherEndType.declaringClass.declaredTypes.findFirst[it != otherEndType]
		val thisEnd = createEnd(factory.createLinkEndData, thisEndType, source.expression)
		val otherEnd = createEnd(factory.createLinkEndData, otherEndType)
		result.endData += #[thisEnd, otherEnd]
		result.createResult(result.name, otherEnd.end.type)
		result.name = '''«thisEnd.value.name» -> «otherEndType.name»'''
	}

}