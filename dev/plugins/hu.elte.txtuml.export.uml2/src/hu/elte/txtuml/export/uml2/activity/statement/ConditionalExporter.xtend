package hu.elte.txtuml.export.uml2.activity.statement

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.activity.expression.PrefixOperatorExporter
import org.eclipse.jdt.core.dom.IfStatement
import org.eclipse.uml2.uml.ConditionalNode

class ConditionalExporter extends ControlExporter<IfStatement, ConditionalNode> {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(IfStatement access) { factory.createConditionalNode }
	
	override exportContents(IfStatement source) {
		val condVar = getVariable(IfExporter.IF_CONDITION_VAR)
		val thenCond = condVar.read
		
		val thenClause = result.createClause
		thenClause.tests += thenCond
		thenClause.decider = thenCond.result

		thenClause.bodies += exportStatement(source.thenStatement)
		if (source.elseStatement != null) {
			val elseCond = new PrefixOperatorExporter(this).logicalNot(condVar.read)
			val elseClause = result.createClause
			elseClause.bodies += exportStatement(source.elseStatement)
			elseClause.tests += elseCond
			elseCond.storeNode
			elseClause.decider = elseCond.result
		}
	}	
}