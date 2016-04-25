package hu.elte.txtuml.export.uml2.activity.statement

import hu.elte.txtuml.export.uml2.activity.statement.ControlExporter
import org.eclipse.jdt.core.dom.ForStatement
import org.eclipse.uml2.uml.LoopNode
import hu.elte.txtuml.export.uml2.BaseExporter

class ForLoopExporter extends ControlExporter<ForStatement, LoopNode> {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(ForStatement access) { factory.createLoopNode }
	
	override exportContents(ForStatement source) {
		result.setupParts += source.initializers.map[exportExpression]
		val expr = exportExpression(source.expression)
		result.tests += expr
		result.decider = expr.result
		result.bodyParts += exportStatement(source.body)
		result.bodyParts += source.updaters.map[exportExpression]
		result.name = '''for («expr.name») { ... }'''
	}
	
}