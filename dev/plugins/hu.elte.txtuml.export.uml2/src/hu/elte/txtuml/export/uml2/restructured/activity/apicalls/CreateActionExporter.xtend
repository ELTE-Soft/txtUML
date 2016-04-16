package hu.elte.txtuml.export.uml2.restructured.activity.apicalls

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.export.uml2.restructured.activity.expression.CreationExporter
import hu.elte.txtuml.export.uml2.restructured.activity.expression.MethodCallExporter
import hu.elte.txtuml.export.uml2.restructured.activity.expression.VariableExpressionExporter
import hu.elte.txtuml.export.uml2.restructured.activity.expression.assign.AssignToVariableExporter
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.jdt.core.dom.TypeLiteral
import org.eclipse.uml2.uml.Action
import org.eclipse.uml2.uml.Class
import org.eclipse.uml2.uml.Operation

class CreateActionExporter extends CreationExporter<MethodInvocation> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(MethodInvocation access) {
		if (isApiMethodInvocation(access.resolveMethodBinding) && access.resolveMethodBinding.name == "create")
			factory.createSequenceNode
	}

	override exportContents(MethodInvocation source) {
		val typeBinding = (source.arguments.get(0) as TypeLiteral).type.resolveBinding
		val createdType = fetchType(typeBinding) as Class

		val create = createObject(typeBinding.name, createdType)

		val tempVar = result.createVariable("#temp", createdType)
		new AssignToVariableExporter(this).createWriteVariableAction(tempVar, create)
		val readVar = new VariableExpressionExporter(this).readVar(tempVar)

		callCtor(typeBinding, source.arguments.tail, readVar)
		new VariableExpressionExporter(this).readVar(tempVar)

		result.name = '''create «typeBinding.name»(...)'''
	}

	def callCtor(ITypeBinding createdType, Iterable<Expression> args, Action create) {
		val ctor = createdType.declaredMethods.findFirst [
			isConstructor && parameterTypes.toList == args.map[resolveTypeBinding].toList
		]
		if (ctor == null) {
			throw new IllegalArgumentException('''The constructor of «createdType.name» with the arguments «args.toList» cannot be found.''')
		}
		new MethodCallExporter(this).createCall(factory.createCallOperationAction, fetchElement(ctor) as Operation,
			create, args).storeNode
	}

}