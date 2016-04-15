package hu.elte.txtuml.export.uml2.restructured.activity.apicalls

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.export.uml2.restructured.activity.expression.MethodCallExporter
import hu.elte.txtuml.export.uml2.restructured.activity.statement.ControlExporter
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.jdt.core.dom.TypeLiteral
import org.eclipse.uml2.uml.Class
import org.eclipse.uml2.uml.SequenceNode

class CreateActionExporter extends ControlExporter<MethodInvocation, SequenceNode> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(MethodInvocation access) {
		if (isApiMethodInvocation(access.resolveMethodBinding) && access.resolveMethodBinding.name == "create")
			factory.createSequenceNode
	}

	override exportContents(MethodInvocation source) {
		val typeBinding = (source.arguments.get(0) as TypeLiteral).type.resolveBinding
		val argTypes = source.arguments.tail.map[type]
		val create = exportCreateObjectAction(typeBinding)
		val createStereotype = getImportedElement("Create")
		val ctor = (fetchElement(typeBinding) as Class).ownedOperations.findFirst [
			appliedStereotypes.contains(createStereotype) && ownedParameters.map[type] == argTypes
		]
		if (ctor != null) {
			new MethodCallExporter(this).createCall(factory.createCallOperationAction, ctor, create, source.arguments)
		}
	}

}