package hu.elte.txtuml.export.uml2.restructured.activity.statement

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import org.eclipse.jdt.core.dom.EnhancedForStatement
import org.eclipse.uml2.uml.ExpansionKind
import org.eclipse.uml2.uml.ExpansionRegion

class ForEachExporter extends ControlExporter<EnhancedForStatement, ExpansionRegion> {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(EnhancedForStatement access) { factory.createExpansionRegion }
	
	override exportContents(EnhancedForStatement source) {
		result.mode = ExpansionKind.ITERATIVE_LITERAL
		val expr = exportExpression(source.expression)
		val expnode = factory.createExpansionNode
		storeNode(expnode)
		expnode.type = fetchType(source.expression.resolveTypeBinding)
		result.inputElements += expnode
		expr.result.objectFlow(expnode)
		expnode.name = '''«expr.name»_expansion'''
		val loopVar = result.createVariable(source.parameter.name.identifier, expnode.type)
		storeVariable(loopVar)
		exportStatement(source.body)
	}
	
}