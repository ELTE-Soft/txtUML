package hu.elte.txtuml.export.uml2.activity

import hu.elte.txtuml.api.model.ModelClass
import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.Exporter
import hu.elte.txtuml.export.uml2.activity.expression.ThisExporter
import hu.elte.txtuml.export.uml2.activity.expression.VariableExpressionExporter
import hu.elte.txtuml.export.uml2.activity.statement.ConditionalExporter
import hu.elte.txtuml.export.uml2.activity.statement.LoopConditionExporter
import hu.elte.txtuml.export.uml2.activity.statement.LoopInitExporter
import hu.elte.txtuml.export.uml2.activity.statement.LoopUpdateExporter
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.ForStatement
import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.IfStatement
import org.eclipse.jdt.core.dom.Statement
import org.eclipse.uml2.uml.Action
import org.eclipse.uml2.uml.ActivityEdge
import org.eclipse.uml2.uml.ActivityNode
import org.eclipse.uml2.uml.CallOperationAction
import org.eclipse.uml2.uml.CreateObjectAction
import org.eclipse.uml2.uml.Element
import org.eclipse.uml2.uml.InputPin
import org.eclipse.uml2.uml.ObjectFlow
import org.eclipse.uml2.uml.OutputPin
import org.eclipse.uml2.uml.ReadLinkAction
import org.eclipse.uml2.uml.ReadSelfAction
import org.eclipse.uml2.uml.ReadStructuralFeatureAction
import org.eclipse.uml2.uml.ReadVariableAction
import org.eclipse.uml2.uml.SequenceNode
import org.eclipse.uml2.uml.TestIdentityAction
import org.eclipse.uml2.uml.Type
import org.eclipse.uml2.uml.ValueSpecificationAction
import org.eclipse.uml2.uml.Variable

import static hu.elte.txtuml.export.uml2.activity.ActionExporter.OperatorType.*

/**
 * Base class for all exporters on the statement-expression level.
 * 
 * Action exporters do not have pre-storage, exportStatement and exportExpression
 * methods will store the elements after the export. This guarantees correct ordering
 * of subexpressions.
 */
abstract class ActionExporter<S, R extends Element> extends Exporter<S, S, R> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	def isApiMethodInvocation(IMethodBinding meth) {
		var type = meth.declaringClass
		type.package.name.startsWith(ModelClass.package.name)
	}

	def void storeEdge(ActivityEdge edge) {
		if (parent instanceof ActionExporter<?, ?>) {
			(parent as ActionExporter<?, ?>).storeEdge(edge)
		} else {
			throw new UnsupportedOperationException("No place to store edge " + edge)
		}
	}

	def void storeNode(ActivityNode node) {
		if (parent instanceof ActionExporter<?, ?>) {
			(parent as ActionExporter<?, ?>).storeNode(node)
		} else {
			throw new UnsupportedOperationException("No place to store node " + node)
		}
	}

	def void storeVariable(Variable variable) {
		if (parent instanceof ActionExporter<?, ?>) {
			(parent as ActionExporter<?, ?>).storeVariable(variable)
		} else {
			throw new UnsupportedOperationException("No place to store variable " + variable)
		}
	}

	def Variable getVariable(String varName) {
		if (parent instanceof ActionExporter<?, ?>) {
			(parent as ActionExporter<?, ?>).getVariable(varName)
		} else {
			throw new UnsupportedOperationException("Variable '" + varName + "' cannot be found")
		}
	}

	def dispatch OutputPin result(ReadVariableAction node) { node.result }

	def dispatch OutputPin result(TestIdentityAction node) { node.result }

	def dispatch OutputPin result(ReadSelfAction node) { node.result }

	def dispatch OutputPin result(ReadLinkAction node) { node.result }

	def dispatch OutputPin result(ReadStructuralFeatureAction node) { node.result }

	def dispatch OutputPin result(CreateObjectAction node) { node.result }

	def dispatch OutputPin result(CallOperationAction node) { node.results.get(0) }

	def dispatch OutputPin result(ValueSpecificationAction node) { node.result }

	def dispatch OutputPin result(SequenceNode node) { (node.executableNodes.last as Action).result }

	def dispatch inputs(ReadLinkAction node) { node.inputValues.map[otherSide].flatten }

	def dispatch inputs(CallOperationAction node) { node.arguments.map[otherSide].flatten }

	def dispatch inputs(ReadStructuralFeatureAction node) { node.object.otherSide }

	def dispatch inputs(Action act) { #[] }

	def getOtherSide(InputPin inp) { inp.incomings.filter[it instanceof ObjectFlow].map[source] }

	def objectFlow(ActivityNode source, ActivityNode target) {
		val flow = factory.createObjectFlow
		flow.storeEdge
		flow.source = source
		flow.target = target
		flow.name = source.name + "==>" + target.name
	}

	def controlFlow(ActivityNode from, ActivityNode to) {
		val edge = factory.createControlFlow
		edge.storeEdge
		edge.name = from.name + "~~>" + to.name
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

	override exportStatement(Statement source) {
		val stmt = super.exportStatement(source)
		stmt?.storeNode
		return stmt
	}

	override exportExpression(Expression source) {
		val expr = super.exportExpression(source)
		expr?.storeNode
		return expr
	}

	def exportConditional(IfStatement source) {
		val expr = cache.export(new ConditionalExporter(this), source, source, [])
		expr?.storeNode
		return expr
	}

	def exportLoopInit(ForStatement source) {
		val init = cache.export(new LoopInitExporter(this), source, source, [])
		storeNode(init)
		return init
	}

	def exportLoopCondition(ForStatement source) {
		val cond = cache.export(new LoopConditionExporter(this), source, source, [])
		storeNode(cond)
		return cond
	}

	def exportLoopUpdate(ForStatement source) {
		val update = cache.export(new LoopUpdateExporter(this), source, source, [])
		storeNode(update)
		return update
	}

	def read(Variable variable) { new VariableExpressionExporter(this).readVar(variable) }

	def write(Variable variable, Action newValue, boolean isReplace) {
		val write = factory.createAddVariableValueAction
		write.name = '''«variable.name»=«newValue.name»'''
		write.isReplaceAll = isReplace
		write.variable = variable
		newValue.result.objectFlow(write.createValue("new_value", variable.type))
		storeNode(write)
		return write
	}

	def thisRef(Type cls) { new ThisExporter(this).createThis(cls) }

	enum OperatorType {
		INTEGER,
		REAL,
		BOOLEAN,
		STRING,
		OBJECT
	}

	def getType(ITypeBinding javaType) {
		switch javaType.name {
			case "int",
			case "Int",
			case "long",
			case "Long",
			case "short",
			case "Short",
			case "byte",
			case "Byte": INTEGER
			case "double",
			case "Double",
			case "float",
			case "Float": REAL
			case "boolean",
			case "Boolean": BOOLEAN
			case "String": STRING
			default: OBJECT
		}
	}

	def combineTypes(OperatorType opt1, OperatorType opt2) {
		switch opt1 {
			case INTEGER:
				switch opt2 {
					case INTEGER: INTEGER
					default: REAL
				}
			default:
				return opt1
		}
	}
}
