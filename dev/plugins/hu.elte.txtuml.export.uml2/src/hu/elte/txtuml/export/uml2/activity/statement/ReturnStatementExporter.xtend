package hu.elte.txtuml.export.uml2.activity.statement

import hu.elte.txtuml.export.uml2.Exporter
import org.eclipse.jdt.core.dom.ReturnStatement
import org.eclipse.uml2.uml.SequenceNode
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import hu.elte.txtuml.export.uml2.utils.MultiplicityProvider

class ReturnStatementExporter extends ControlExporter<ReturnStatement, SequenceNode> {

	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(ReturnStatement access) { factory.createSequenceNode }

	override exportContents(ReturnStatement source) {
		val resBinding = source.expression.resolveTypeBinding
		var low = 1
		var up = 1
		if(ElementTypeTeller.isCollection(resBinding)) {
			 low = MultiplicityProvider.getLowerBound(resBinding)
			 up = MultiplicityProvider.getUpperBound(resBinding)
		}
		val ret = exportExpression(source.expression)
		val retVar = getVariable("return")
		retVar.lower = low
		retVar.upper = up
		
		retVar.write(ret, true)
		result.name = "return " + ret.name
	}

}