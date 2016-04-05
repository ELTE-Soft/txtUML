package hu.elte.txtuml.export.uml2.restructured.activity.expression.assign

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.export.uml2.restructured.activity.expression.VariableExpressionExporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import org.eclipse.jdt.core.dom.Assignment
import org.eclipse.jdt.core.dom.SimpleName
import org.eclipse.uml2.uml.Action
import org.eclipse.uml2.uml.Variable

class AssignToVariableExporter extends AssignExporter {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(Assignment access) {
		if(ElementTypeTeller.isVariable(access.leftHandSide)) factory.createSequenceNode
	}

	override exportContents(Assignment source) {
		val varName = (source.leftHandSide as SimpleName).identifier
		val rhsVal = exportExpression(source.rightHandSide)
		val assignVar = getVariable(varName)
		val rhs = generateRhs(source.operator, [new VariableExpressionExporter(this).readVar(assignVar)], rhsVal)
		val write = createWriteVariableAction(assignVar, rhs)
		result.name = write.name
		new VariableExpressionExporter(this).readVar(assignVar)
	}

	def createWriteVariableAction(Variable variable, Action rhs) {
		val write = factory.createAddVariableValueAction
		write.name = '''«variable.name»=«rhs.name»'''
		write.isReplaceAll = true
		write.variable = variable
		rhs.result.objectFlow(write.createValue("new_value", variable.type))
		storeNode(write)
		return write
	}

}