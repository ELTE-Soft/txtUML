package hu.elte.txtuml.export.uml2.restructured.activity

import org.eclipse.jdt.core.dom.IfStatement
import org.eclipse.uml2.uml.ConditionalNode
import org.eclipse.uml2.uml.Element

class IfExporter extends ControlExporter<IfStatement, ConditionalNode> {

	override create(IfStatement access) { factory.createConditionalNode }

	override exportContents(IfStatement source) {
		val variable = factory.createVariable
		variable.name = "#if_cond"
		variable.type = booleanType
		parent.store(variable)
		val thenClause = factory.createClause
		result.clauses.add(thenClause)
		val testExpr = exportExpression(source.expression)
		result.setName("if_" + testExpr.name)
		thenClause.tests.add(testExpr)
		parent.store(variable.writeVariable(testExpr))

		thenClause.bodies.add(exportStatement(source.thenStatement))
		if (source.elseStatement != null) {
			val elseClause = factory.createClause
			elseClause.bodies.add(exportStatement(source.elseStatement))
			result.clauses.add(elseClause)
		}
	}

	override tryStore(Element contained) { false }

}