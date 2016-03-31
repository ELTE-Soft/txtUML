package hu.elte.txtuml.export.uml2.restructured.activity

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.export.uml2.restructured.Exporter
import org.eclipse.uml2.uml.Action
import org.eclipse.uml2.uml.ActivityEdge
import org.eclipse.uml2.uml.ActivityNode
import org.eclipse.uml2.uml.CallOperationAction
import org.eclipse.uml2.uml.Element
import org.eclipse.uml2.uml.InputPin
import org.eclipse.uml2.uml.ObjectFlow
import org.eclipse.uml2.uml.OutputPin
import org.eclipse.uml2.uml.ReadLinkAction
import org.eclipse.uml2.uml.ReadSelfAction
import org.eclipse.uml2.uml.ReadVariableAction
import org.eclipse.uml2.uml.ValueSpecificationAction
import org.eclipse.uml2.uml.Variable

abstract class ActionExporter<S, R extends Element> extends Exporter<S, S, R> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	def void storeEdge(ActivityEdge edge) {
		(parent as ActionExporter<?, ?>).storeEdge(edge)
	}
	
	def void storeNode(ActivityNode node) {
		(parent as ActionExporter<?, ?>).storeNode(node)
	}
	
	def void storeVariable(Variable variable) {
		(parent as ActionExporter<?, ?>).storeVariable(variable)
	}

	def dispatch result(ReadVariableAction node) { node.result }

	def dispatch result(ReadSelfAction node) { node.result }

	def dispatch result(ReadLinkAction node) { node.result }

	def dispatch result(CallOperationAction node) { node.results.get(0) }
	
	def dispatch result(ValueSpecificationAction node) { node.result }

	def dispatch inputs(ReadLinkAction node) { node.inputValues.map[otherSide].flatten }

	def dispatch inputs(CallOperationAction node) { node.arguments.map[otherSide].flatten }

	def dispatch inputs(Action act) { #[] }

	def getOtherSide(InputPin inp) { inp.incomings.filter[it instanceof ObjectFlow].map[source] }

	def connect(OutputPin source, InputPin target) {
		val flow = factory.createObjectFlow
		flow.storeEdge
		flow.source = source
		flow.target = target
		flow.name = "object_flow_from_" + source.name + "_to_" + target.name
	}
	
	def controlFlow(ActivityNode from, ActivityNode to) {
		val edge = factory.createControlFlow
		edge.storeEdge
		edge.name = "control_flow_from_" + from.name + "_to_" + to.name
		edge.source = from
		edge.target = to
		return edge
	}

	def store(Action action) {
		val subExpressions = action.inputs.map[otherSide].flatten
		subExpressions.forEach[storeNode]
		storeNode(action)
		return action
	}

}
