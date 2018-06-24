package hu.elte.txtuml.export.uml2.activity.apicalls

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.activity.expression.OperatorExporter
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.uml2.uml.Variable

class CollectExporter extends OperatorExporter<MethodInvocation> {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(MethodInvocation access) {
		if (isApiMethodInvocation(access.resolveMethodBinding) && access.resolveMethodBinding.name == "collect")
			factory.createSequenceNode
	}
	
	override exportContents(MethodInvocation source) {
		
		val firstExpression = source.arguments.get(0) as Expression
		val exportedExpr = firstExpression.exportExpression	
		val createdType = exportedExpr.type 

		val tempVar = result.createVariable("#temp", createdType)
		tempVar.lower = 0
		tempVar.upper = -1
		addAllParameter(tempVar, source.arguments)	
		result.name = '''create «exportedExpr.name»'''
	}
	
	def addAllParameter(Variable tempVar, Iterable<Expression> args) {
		args.forEach[ expr | 
			delayWhen(false, [tempVar.read]) [
				val rhs = exportExpression(expr)
				val write = tempVar.write(rhs, false)
				result.name = write.name
			]
		]
	}
	
	
}