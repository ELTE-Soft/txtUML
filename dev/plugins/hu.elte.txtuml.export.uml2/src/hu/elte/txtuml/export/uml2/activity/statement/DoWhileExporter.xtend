package hu.elte.txtuml.export.uml2.activity.statement

import hu.elte.txtuml.export.uml2.BaseExporter
import org.eclipse.jdt.core.dom.DoStatement
import org.eclipse.uml2.uml.LoopNode

class DoWhileExporter extends ControlExporter<DoStatement,LoopNode> {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(DoStatement access) { factory.createLoopNode }
	
	override exportContents(DoStatement source) {
		val expr = exportExpression(source.expression)
		result.name = '''while («expr.name») { ... }'''
		result.setupParts += exportStatement(source.body)
		result.tests += expr
		result.decider = expr.result
		result.bodyParts += exportStatement(source.body)
	}	
}