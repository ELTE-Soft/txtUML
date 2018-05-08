package hu.elte.txtuml.export.uml2.activity.apicalls

import org.eclipse.jdt.core.dom.MethodInvocation
import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.activity.expression.OperatorExporter
import org.eclipse.jdt.core.dom.Expression

class AddToMultipliedElementExporter extends OperatorExporter<MethodInvocation> {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}	
	
	override create(MethodInvocation access) {
		if (isApiMethodInvocation(access.resolveMethodBinding) && 
			access.resolveMethodBinding.name == "add"
		) {
			factory.createSequenceNode
		}
	
	}
	
	override exportContents(MethodInvocation source) {
			assignToExpression(source.expression)[ act |
			val valueExpr  = exportExpression(source.arguments.get(0) as Expression)	
			valueExpr
		]
	}
	
}