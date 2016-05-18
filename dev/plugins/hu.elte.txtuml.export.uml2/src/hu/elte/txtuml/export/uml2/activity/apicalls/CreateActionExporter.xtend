package hu.elte.txtuml.export.uml2.activity.apicalls

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.activity.expression.CreationExporter
import hu.elte.txtuml.export.uml2.activity.expression.MethodCallExporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import hu.elte.txtuml.utils.jdt.SharedUtils
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.jdt.core.dom.TypeLiteral
import org.eclipse.uml2.uml.Action
import org.eclipse.uml2.uml.Class
import java.util.LinkedList

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
		tempVar.write(create)

		callCtor(typeBinding, source.arguments.tail, tempVar.read)
		tempVar.read
		result.name = '''create «typeBinding.name»'''
	}

	def callCtor(ITypeBinding createdType, Iterable<Expression> args, Action create) {
		val ctor = createdType.declaredMethods.findFirst [
			isConstructor && SharedUtils.isApplicableToCall(args.map[resolveTypeBinding], it)
		]
		if (ctor == null) {
			throw new IllegalArgumentException('''The constructor of «createdType.name» with the arguments «args.toList» cannot be found.''')
		}
		if (ElementTypeTeller.isSignal(ctor.declaringClass)) {
			val newArgs = new LinkedList(args.map[exportExpression].toList)
			newArgs.add(0, create)
			new MethodCallExporter(this).createCallFromActions(factory.createCallOperationAction,
				ctor, null, newArgs).storeNode
		} else {
			new MethodCallExporter(this).createCall(factory.createCallOperationAction,
				ctor, create, args).storeNode
		}
		new MethodCallExporter(this).createCall(factory.createCallOperationAction, ctor,
			create, args).storeNode
	}

}