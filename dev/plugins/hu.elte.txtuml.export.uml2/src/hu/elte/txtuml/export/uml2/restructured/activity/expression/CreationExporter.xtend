package hu.elte.txtuml.export.uml2.restructured.activity.expression

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.export.uml2.restructured.activity.statement.ControlExporter
import org.eclipse.uml2.uml.Classifier
import org.eclipse.uml2.uml.SequenceNode

abstract class CreationExporter<T> extends ControlExporter<T, SequenceNode> {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	def createObject(String name, Classifier createdType) {
		val create = factory.createCreateObjectAction
		create.classifier = createdType
		create.name = '''instantiate «name»'''
		create.createResult(create.name, create.classifier)
		storeNode(create)
		return create
	}
	
}