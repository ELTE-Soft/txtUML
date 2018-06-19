package hu.elte.txtuml.export.uml2.activity.expression

import hu.elte.txtuml.export.uml2.Exporter
import hu.elte.txtuml.export.uml2.activity.ActionExporter
import java.util.List
import java.util.concurrent.atomic.AtomicInteger
import org.eclipse.jdt.core.dom.ConstructorInvocation
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.jdt.core.dom.Modifier
import org.eclipse.jdt.core.dom.SuperConstructorInvocation
import org.eclipse.jdt.core.dom.SuperMethodInvocation
import org.eclipse.uml2.uml.Action
import org.eclipse.uml2.uml.CallOperationAction
import org.eclipse.uml2.uml.Operation
import org.eclipse.uml2.uml.SequenceNode
import hu.elte.txtuml.utils.jdt.ElementTypeTeller

abstract class CallExporter<T> extends ActionExporter<T, CallOperationAction> {

	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override CallOperationAction create(T access) {
		if(!isApiMethodInvocation(access.binding)) factory.createCallOperationAction
	}

	def List<Expression> getArguments(T inv)

	def Expression getExpression(T inv)

	def IMethodBinding getBinding(T inv)

	override void exportContents(T source) {
		val binding = source.binding
		if (!Modifier.isStatic(binding.modifiers)) {
			val target = exportExpression(source.expression) ?: thisRef(binding.declaringClass.fetchType)

			createCall(result, binding, target, source.arguments)
		} else {
			createCall(result, binding, null, source.arguments)
		}
	}

	def createCall(IMethodBinding binding, Action base, Iterable<Expression> args) {
		val call = factory.createCallOperationAction
		createCallFromActions(call, binding, base, args.map[exportExpression])
		storeNode(call)
		return call
	}

	def createCall(CallOperationAction call, IMethodBinding binding, Action base, Iterable<Expression> args) {
		createCallFromActions(call, binding, base, args.map[exportExpression])
	}
	
	def createCallFromActions(CallOperationAction call, IMethodBinding binding, Action base, Iterable<Action> args) {
		call.operation = fetchElement(binding) as Operation

		if (base != null) {
			base.result.objectFlow(call.createTarget("target", base.result.type))
		}

		val i = new AtomicInteger
		args.forEach[call.createArgument("p" + i.andIncrement, it.result.type)]

		if (binding.returnType.name != "void") {
			val resType = if(ElementTypeTeller.isCollection(binding.returnType)) fetchType(binding.returnType.typeArguments.get(0)) else fetchType(binding.returnType)
			call.createResult("return", resType)
		}

		val argVals = args.map[it.result]

		for (argi : 0 ..< call.arguments.length) {
			argVals.get(argi).objectFlow(call.arguments.get(argi))
		}
		call.name = buildName(binding, call, base).toString
		return call
	}

	def buildName(IMethodBinding binding, CallOperationAction call, Action base) {
		'''«base?.name?.concat('.')»«binding.name»(«buildArgs(call)»)'''
	}

	def buildArgs(CallOperationAction call) {
		'''«FOR arg : call.getArguments SEPARATOR ", "»«arg?.type?.name» «arg.name»«ENDFOR»'''
	}

}

class MethodCallExporter extends CallExporter<MethodInvocation> {

	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override getArguments(MethodInvocation inv) { inv.arguments }

	override getExpression(MethodInvocation inv) { inv.expression }

	override getBinding(MethodInvocation inv) { inv.resolveMethodBinding }
}

class SuperCallExporter extends CallExporter<SuperMethodInvocation> {

	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override getArguments(SuperMethodInvocation inv) { inv.arguments }

	override getExpression(SuperMethodInvocation inv) { null }

	override getBinding(SuperMethodInvocation inv) { inv.resolveMethodBinding }
}

class OtherCtorCallExporter extends CallExporter<ConstructorInvocation> {

	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override getArguments(ConstructorInvocation inv) { inv.arguments }

	override getExpression(ConstructorInvocation inv) { null }

	override getBinding(ConstructorInvocation inv) { inv.resolveConstructorBinding }
}

class SuperCtorCallExporter extends CallExporter<SuperConstructorInvocation> {

	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override getArguments(SuperConstructorInvocation inv) { inv.arguments }

	override getExpression(SuperConstructorInvocation inv) { null }

	override getBinding(SuperConstructorInvocation inv) { inv.resolveConstructorBinding }
}

class APISuperCtorCallExporter extends ActionExporter<SuperConstructorInvocation, SequenceNode> {

	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(SuperConstructorInvocation access) {
		if(isApiMethodInvocation(access.resolveConstructorBinding)) factory.createSequenceNode
	}

	override exportContents(SuperConstructorInvocation source) {
		result.name = '#api_ctor_call'
	}

}
