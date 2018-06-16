package hu.elte.txtuml.export.uml2.activity.statement

import hu.elte.txtuml.export.uml2.Exporter
import org.eclipse.jdt.core.dom.IfStatement
import org.eclipse.uml2.uml.Operation
import org.eclipse.uml2.uml.ParameterDirectionKind
import org.eclipse.uml2.uml.SequenceNode
import org.eclipse.uml2.uml.Type

class IfExporter extends ControlExporter<IfStatement, SequenceNode> {

	public static val IF_CONDITION_VAR = "#if_cond"

	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(IfStatement access) { factory.createSequenceNode }

	override exportContents(IfStatement source) {
		
		val condVar = result.createVariable(IF_CONDITION_VAR, booleanType)
		val testExpr = exportExpression(source.expression)
		result.nodes += write(condVar, testExpr, true)
		result.name = '''if («testExpr.name»)'''

		exportConditional(source)
	}

	def Type getReturnType(Operation operation) {
		operation.ownedParameters.findFirst[direction == ParameterDirectionKind.RETURN_LITERAL].type
	}

}