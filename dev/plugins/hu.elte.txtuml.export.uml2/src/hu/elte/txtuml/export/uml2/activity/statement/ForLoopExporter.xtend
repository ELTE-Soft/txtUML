package hu.elte.txtuml.export.uml2.activity.statement

import hu.elte.txtuml.export.uml2.activity.statement.ControlExporter
import org.eclipse.jdt.core.dom.ForStatement
import org.eclipse.uml2.uml.LoopNode
import hu.elte.txtuml.export.uml2.BaseExporter
import org.eclipse.uml2.uml.SequenceNode
import org.eclipse.uml2.uml.Variable
import hu.elte.txtuml.export.uml2.activity.ActionExporter

class ForLoopExporter extends ControlExporter<ForStatement, LoopNode> {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(ForStatement access) { factory.createLoopNode }
	
	override exportContents(ForStatement source) {
		result.setupParts += exportLoopInit(source)
		val cond = exportLoopCondition(source)
		result.tests += cond
		result.decider = cond.result
		result.bodyParts += exportStatement(source.body)
		result.bodyParts += exportLoopUpdate(source)
		result.name = '''for («cond.name») { ... }'''
	}
	
}

class LoopInitExporter extends ControlExporter<ForStatement, SequenceNode> {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(ForStatement access) { factory.createSequenceNode }
	
	override exportContents(ForStatement source) {
		source.initializers.forEach[exportExpression]
		result.name = "setup"
	}
	
	override storeVariable(Variable variable) {
		(parent as ActionExporter<?,?>).storeVariable(variable)
	}
}

class LoopConditionExporter extends ControlExporter<ForStatement, SequenceNode> {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(ForStatement access) { factory.createSequenceNode }
	
	override exportContents(ForStatement source) {
		val expr = exportExpression(source.expression)
		result.name = expr.name
	}
}

class LoopUpdateExporter extends ControlExporter<ForStatement, SequenceNode> {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(ForStatement access) { factory.createSequenceNode }
	
	override exportContents(ForStatement source) {
		source.updaters.forEach[exportExpression]
		result.name = "update"
	}
}