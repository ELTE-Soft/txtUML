package hu.elte.txtuml.export.uml2.activity.apicalls

import hu.elte.txtuml.export.uml2.activity.ActionExporter
import org.eclipse.jdt.core.dom.MethodInvocation
import hu.elte.txtuml.export.uml2.BaseExporter
import org.eclipse.uml2.uml.DestroyObjectAction
import org.eclipse.jdt.core.dom.Expression

class DeleteActionExporter extends ActionExporter<MethodInvocation, DestroyObjectAction> {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(MethodInvocation access) {
		if (isApiMethodInvocation(access.resolveMethodBinding) && access.resolveMethodBinding.name == "delete")
			factory.createDestroyObjectAction
	}
	
	override exportContents(MethodInvocation source) {
		val arg = source.arguments.get(0) as Expression
		val target = result.createTarget("destroyed", fetchType(arg.resolveTypeBinding))
		val expr = exportExpression(arg)
		expr.objectFlow(target)
		result.name = '''delete «expr.name»'''
	}
	
}