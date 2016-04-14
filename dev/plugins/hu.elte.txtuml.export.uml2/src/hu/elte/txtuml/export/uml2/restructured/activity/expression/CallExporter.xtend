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
import org.eclipse.uml2.uml.CallOperationAction

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
			val target = exportExpression(source.expression) ?:
				new ThisExporter(this).createThis(binding.declaringClass)
			target.result.objectFlow(result.createTarget(target.name, target.result.type))
		}

		val i = new AtomicInteger
		binding.parameterTypes.forEach[result.createArgument("p" + i.andIncrement, fetchType)]

		val argVals = source.arguments.map[exportExpression].map[it.result]

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
