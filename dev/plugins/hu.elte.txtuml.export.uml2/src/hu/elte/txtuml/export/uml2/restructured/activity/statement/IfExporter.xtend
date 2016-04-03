package hu.elte.txtuml.export.uml2.restructured.activity.statement

import hu.elte.txtuml.export.uml2.restructured.Exporter
import org.eclipse.jdt.core.dom.IfStatement
import org.eclipse.uml2.uml.ConditionalNode
import org.eclipse.uml2.uml.Operation
import org.eclipse.uml2.uml.ParameterDirectionKind
import org.eclipse.uml2.uml.ReadVariableAction
import org.eclipse.uml2.uml.SequenceNode
import org.eclipse.uml2.uml.Type
import org.eclipse.uml2.uml.UMLPackage
import org.eclipse.uml2.uml.Variable

class IfExporter extends ControlExporter<IfStatement, SequenceNode> {

	static val LOGICAL_NOT = "not"
	
	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(IfStatement access) { factory.createSequenceNode }

	override exportContents(IfStatement source) {

		val condVar = result.createVariable("#if_cond", booleanType)
		val testExpr = exportExpression(source.expression)
		result.nodes += testExpr
		result.nodes += writeVariable(condVar, testExpr)

		val condNode = result.createNode("if_" + testExpr.name, UMLPackage.Literals.CONDITIONAL_NODE) as ConditionalNode
		val thenClause = condNode.createClause
		val readVar = condVar.read
		thenClause.tests += readVar
		thenClause.decider = condVar.read.store.result

		thenClause.bodies += exportStatement(source.thenStatement)
		if (source.elseStatement != null) {
			val elseClause = condNode.createClause
			elseClause.bodies += exportStatement(source.elseStatement)
			elseClause.tests += readVar
			elseClause.decider = LOGICAL_NOT.eval(condVar.read).store.result
		}
	}

	def eval(String opName, ReadVariableAction... action) {
		eval(getImportedElement(opName) as Operation, action)
	}

	def eval(Operation op, ReadVariableAction... action) {
		val act = factory.createCallOperationAction
		act.operation = op
		act.arguments += action.map[it.result]
		return act
	}
	
	def Type getReturnType(Operation operation) {
		operation.ownedParameters.findFirst[direction == ParameterDirectionKind.RETURN_LITERAL].type
	}

	def read(Variable variable) {
		val readVar = factory.createReadVariableAction
		readVar.variable = variable
		readVar.createResult("read_" + variable.name, variable.type)
		return readVar
	}

}