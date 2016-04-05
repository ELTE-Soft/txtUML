package hu.elte.txtuml.export.uml2.restructured.activity.expression

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.export.uml2.restructured.activity.ActionExporter
import org.eclipse.jdt.core.dom.ThisExpression
import org.eclipse.uml2.uml.ReadSelfAction
import org.eclipse.jdt.core.dom.ITypeBinding

class ThisExporter extends ActionExporter<ThisExpression, ReadSelfAction> {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(ThisExpression access) { factory.createReadSelfAction }
	
	override exportContents(ThisExpression source) {
		result.name = "this"
		result.createResult("this", fetchType(source.resolveTypeBinding))
	}
	
	def createThis(ITypeBinding ref) {
		val readThis = factory.createReadSelfAction
		readThis.name = "this"
		readThis.createResult("self_result", fetchType(ref))
		storeNode(readThis)
		readThis
	}
	
}