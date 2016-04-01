package hu.elte.txtuml.export.uml2.restructured.activity.expression

import hu.elte.txtuml.export.uml2.restructured.Exporter
import java.util.concurrent.atomic.AtomicInteger
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.jdt.core.dom.Modifier
import org.eclipse.uml2.uml.CallOperationAction

class CallExporter extends hu.elte.txtuml.export.uml2.restructured.activity.ActionExporter<MethodInvocation, CallOperationAction> {

	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override CallOperationAction create(MethodInvocation access) {
		if(!isApiMethodInvocation(access)) factory.createCallOperationAction
	}

	def isApiMethodInvocation(MethodInvocation invocation) {
		val meth = invocation.resolveMethodBinding
		val type = meth.declaringClass
		API_CLASSES.contains(type.qualifiedName)
	}

	override void exportContents(MethodInvocation source) {
		val binding = source.resolveMethodBinding
		if (!Modifier.isStatic(binding.modifiers)) {
			val target = exportExpression(source.expression)[storeNode] ?: createThis(binding.declaringClass)
			target.result.objectFlow(result.createTarget(target.name, target.result.type))
		}

		val i = new AtomicInteger
		binding.parameterTypes.forEach[result.createArgument("p" + i.andIncrement, fetchType)]

		val argVals = source.arguments.map[exportExpression[storeNode]].map[it.result]

		for (argi : 0 ..< result.arguments.length) {
			argVals.get(argi).objectFlow(result.arguments.get(argi))
		}
	}

	def buildName() '''«result.target?.name?.concat(".")»«result.operation?.name»(«buildArgs»)'''

	def buildArgs() '''«FOR arg : result.arguments SEPARATOR ", "»«arg.type.name» «arg.name»«ENDFOR»'''

	def createThis(ITypeBinding ref) {
		val readThis = factory.createReadSelfAction
		readThis.createResult("self_result", fetchType(ref))
		storeNode(readThis)
		readThis
	}

}
