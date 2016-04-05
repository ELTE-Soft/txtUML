package hu.elte.txtuml.export.uml2.restructured.activity.expression.assign

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import org.eclipse.jdt.core.dom.Assignment
import org.eclipse.jdt.core.dom.SimpleName
import org.eclipse.uml2.uml.AddVariableValueAction
import org.eclipse.uml2.uml.ReadVariableAction
import org.eclipse.uml2.uml.UMLPackage

class AssignToVariableExporter extends AssignExporter {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(Assignment access) {
		if (ElementTypeTeller.isVariable(access.leftHandSide)) factory.createSequenceNode
	}

	override exportContents(Assignment source) {
		val varName = (source.leftHandSide as SimpleName).identifier
		val rhs = exportExpression(source.rightHandSide)
		val writeVar = result.createNode('''«varName»«source.operator»«rhs.name»''',
			UMLPackage.Literals.ADD_VARIABLE_VALUE_ACTION) as AddVariableValueAction
		writeVar.isReplaceAll = true
		val assignVar = getVariable(varName)
		writeVar.variable = assignVar
		result.name = writeVar.name
		rhs.result.objectFlow(writeVar.createValue(result.name, assignVar.type))
		val readVar = result.createNode(result.name, UMLPackage.Literals.READ_VARIABLE_ACTION) as ReadVariableAction
		readVar.variable = assignVar
		readVar.createResult(result.name + "_result", assignVar.type)
	}
}