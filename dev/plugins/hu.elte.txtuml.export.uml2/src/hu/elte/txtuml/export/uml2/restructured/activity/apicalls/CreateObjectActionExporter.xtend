package hu.elte.txtuml.export.uml2.restructured.activity.apicalls

import hu.elte.txtuml.export.uml2.restructured.activity.ActionExporter
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.uml2.uml.CreateObjectAction
import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import org.eclipse.uml2.uml.Classifier

class CreateObjectActionExporter extends ActionExporter<ITypeBinding, CreateObjectAction> {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(ITypeBinding access) {
		factory.createCreateObjectAction
	}
	
	override exportContents(ITypeBinding source) {
		result.classifier = fetchElement(source) as Classifier
		result.name = '''instantiate «source.name»'''
	}
	
}