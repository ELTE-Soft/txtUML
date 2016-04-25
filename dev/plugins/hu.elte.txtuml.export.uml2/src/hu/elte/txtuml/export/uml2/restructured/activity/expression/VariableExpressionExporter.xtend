package hu.elte.txtuml.export.uml2.restructured.activity.expression

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.export.uml2.restructured.activity.ActionExporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import org.eclipse.jdt.core.dom.Name
import org.eclipse.uml2.uml.ReadVariableAction
import org.eclipse.uml2.uml.Variable

class VariableExpressionExporter extends ActionExporter<Name, ReadVariableAction> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(Name access) {
		if (ElementTypeTeller.isVariable(access)) factory.createReadVariableAction
	}

	override exportContents(Name source) {
		val variable = getVariable(source.resolveBinding.name)
		finishReadVarAction(result, variable)
	}
			
	def readVar(Variable variable) {
		val ret = factory.createReadVariableAction
		storeNode(ret)
		finishReadVarAction(ret, variable)
		return ret
	}
		
	protected def finishReadVarAction(ReadVariableAction action, Variable variable) {
		action.variable = variable
		action.name = variable.name
		action.createResult(action.name, variable.type)
	}
}