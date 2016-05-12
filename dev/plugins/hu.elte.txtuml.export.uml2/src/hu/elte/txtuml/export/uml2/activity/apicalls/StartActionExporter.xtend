package hu.elte.txtuml.export.uml2.activity.apicalls

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.activity.ActionExporter
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.uml2.uml.StartObjectBehaviorAction
import org.eclipse.jdt.core.dom.Expression

class StartActionExporter extends ActionExporter<MethodInvocation, StartObjectBehaviorAction> {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(MethodInvocation access) {
		if (isApiMethodInvocation(access.resolveMethodBinding) && access.resolveMethodBinding.name == "start")
			factory.createStartObjectBehaviorAction
	}
	
	override exportContents(MethodInvocation source) {
		val expr = exportExpression(source.arguments.get(0) as Expression)
		expr.result.objectFlow(result.createObject(expr.name, expr.result.type))
		result.name = '''start «expr.name»'''
	}
	
}