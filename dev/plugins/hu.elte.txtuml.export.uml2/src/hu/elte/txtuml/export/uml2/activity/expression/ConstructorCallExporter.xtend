package hu.elte.txtuml.export.uml2.activity.expression

import hu.elte.txtuml.export.uml2.BaseExporter
import org.eclipse.jdt.core.dom.ClassInstanceCreation
import org.eclipse.uml2.uml.Classifier

class ConstructorCallExporter extends CreationExporter<ClassInstanceCreation> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(ClassInstanceCreation access) { factory.createSequenceNode }

	override exportContents(ClassInstanceCreation source) {
		val ctorBinding = source.resolveConstructorBinding
		val createdType = fetchType(ctorBinding.declaringClass) as Classifier

		val create = createObject(ctorBinding.declaringClass.name, createdType)

		val tempVar = result.createVariable("#temp", createdType)
		tempVar.write(create)

		new MethodCallExporter(this).createCall(factory.createCallOperationAction,
			ctorBinding, tempVar.read, source.arguments).storeNode
		tempVar.read

		result.name = '''create «ctorBinding.declaringClass.name»'''
	}

}