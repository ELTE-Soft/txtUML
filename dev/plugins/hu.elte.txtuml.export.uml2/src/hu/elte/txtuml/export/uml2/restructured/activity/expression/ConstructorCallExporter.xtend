package hu.elte.txtuml.export.uml2.restructured.activity.expression

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.export.uml2.restructured.activity.expression.MethodCallExporter
import hu.elte.txtuml.export.uml2.restructured.activity.expression.VariableExpressionExporter
import hu.elte.txtuml.export.uml2.restructured.activity.expression.assign.AssignToVariableExporter
import org.eclipse.jdt.core.dom.ConstructorInvocation
import org.eclipse.uml2.uml.Class
import org.eclipse.uml2.uml.Operation

class ConstructorCallExporter extends CreationExporter<ConstructorInvocation> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(ConstructorInvocation access) { factory.createSequenceNode }

	override exportContents(ConstructorInvocation source) {
		val ctorBinding = source.resolveConstructorBinding
		val createdType = fetchType(ctorBinding.declaringClass) as Class

		val create = createObject(ctorBinding.declaringClass.name, createdType)

		val tempVar = result.createVariable("#temp", createdType)
		new AssignToVariableExporter(this).createWriteVariableAction(tempVar, create)
		val readVar = new VariableExpressionExporter(this).readVar(tempVar)

		new MethodCallExporter(this).createCall(factory.createCallOperationAction, fetchElement(ctorBinding) as Operation,
			readVar, source.arguments).storeNode
		new VariableExpressionExporter(this).readVar(tempVar)

		result.name = '''create «ctorBinding.declaringClass.name»(...)'''
	}
	
}