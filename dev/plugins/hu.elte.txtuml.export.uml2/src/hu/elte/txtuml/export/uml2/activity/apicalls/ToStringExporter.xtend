package hu.elte.txtuml.export.uml2.activity.apicalls

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.activity.ActionExporter
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.uml2.uml.Action
import org.eclipse.uml2.uml.CallOperationAction

class ToStringExporter extends ActionExporter<MethodInvocation, CallOperationAction> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(MethodInvocation access) {
		if(access.resolveMethodBinding.name == "toString" && access.arguments.isEmpty) factory.createCallOperationAction
	}

	override exportContents(MethodInvocation source) {
		val action = exportExpression(source.expression)
		createToString(result, action)
	}

	def createToString(Action action) {
		val res = createToString(factory.createCallOperationAction, action)
		storeNode(res)
		return res
	}

	def createToString(CallOperationAction ret, Action action) {
		ret.name = '''«action.name».toString'''
		ret.operation = getImportedOperation("ObjectOperations", "toString")
		val arg = ret.createArgument("input", action.result.type)
		action.result.objectFlow(arg)
		ret.createResult("res", stringType)
		return ret
	}
}

class PrimitiveToStringExporter extends ActionExporter<MethodInvocation, CallOperationAction> {

	private val TO_STRING_CLASSES = #[Boolean, Byte, Short, Integer, Long, Float, Double].map[canonicalName]

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(MethodInvocation access) {
		if (TO_STRING_CLASSES.contains(access.resolveMethodBinding.declaringClass.qualifiedName) &&
			access.resolveMethodBinding.name == "toString" && access.arguments.size == 1)
			factory.createCallOperationAction
	}

	override exportContents(MethodInvocation source) {
		val action = exportExpression(source.arguments.get(0) as Expression)
		new ToStringExporter(this).createToString(result, action)
	}
}