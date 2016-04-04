package hu.elte.txtuml.export.uml2.restructured.activity.statement

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import org.eclipse.jdt.core.dom.WhileStatement
import org.eclipse.uml2.uml.LoopNode

class WhileExporter extends ControlExporter<WhileStatement,LoopNode> {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(WhileStatement access) { factory.createLoopNode }
	
	override exportContents(WhileStatement source) {
		val expr = exportExpression(source.expression)
		result.name = '''while («expr.name») { ... }'''
		result.tests += expr
		result.decider = expr.result
		result.bodyParts += exportStatement(source.body)
	}	
}