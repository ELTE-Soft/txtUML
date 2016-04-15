package hu.elte.txtuml.export.uml2.restructured.activity.expression

import hu.elte.txtuml.export.uml2.restructured.Exporter
import hu.elte.txtuml.export.uml2.restructured.activity.ActionExporter
import java.util.List
import java.util.concurrent.atomic.AtomicInteger
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.jdt.core.dom.Modifier
import org.eclipse.jdt.core.dom.SuperMethodInvocation
import org.eclipse.uml2.uml.Action
import org.eclipse.uml2.uml.CallOperationAction
import org.eclipse.uml2.uml.Operation
import org.eclipse.jdt.core.dom.ConstructorInvocation

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
		val operation = fetchElement(binding) as Operation
		if (!Modifier.isStatic(binding.modifiers)) {
			val target = exportExpression(source.expression) ?:
				new ThisExporter(this).createThis(binding.declaringClass.fetchType)
			
			createCall(result, operation, target, source.arguments)
		} else {
			createCall(result, operation, null, source.arguments)
		}
	}
	
	def createCall(CallOperationAction call, Operation operation, Action base, List<Expression> args) {
		result.operation = operation
		
		base?.result.objectFlow(result.createTarget(base.name, base.result.type))
		
		val i = new AtomicInteger
		args.forEach[result.createArgument("p" + i.andIncrement, resolveTypeBinding.fetchType)]

		val argVals = args.map[exportExpression].map[it.result]

		for (argi : 0 ..< result.arguments.length) {
			argVals.get(argi).objectFlow(result.arguments.get(argi))
		}
	}

	def buildName() '''«result.target?.name?.concat(".")»«result.operation?.name»(«buildArgs»)'''

	def buildArgs() '''«FOR arg : result.getArguments SEPARATOR ", "»«arg.type.name» «arg.name»«ENDFOR»'''

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

class ConstructorCallExporter extends CallExporter<ConstructorInvocation> {

	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override getArguments(ConstructorInvocation inv) { inv.arguments }

	override getExpression(ConstructorInvocation inv) { null }

	override getBinding(ConstructorInvocation inv) { inv.resolveConstructorBinding }
}

