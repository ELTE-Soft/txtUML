package hu.elte.txtuml.export.uml2.restructured.statemachine

import hu.elte.txtuml.export.uml2.restructured.Exporter
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.uml2.uml.Transition
import org.eclipse.uml2.uml.Element
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import hu.elte.txtuml.utils.jdt.SharedUtils
import hu.elte.txtuml.api.model.From
import hu.elte.txtuml.api.model.To

class TransitionExporter extends Exporter<TypeDeclaration, ITypeBinding, Transition> {
	
	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(ITypeBinding access) {
		if (ElementTypeTeller.isTransition(access)) factory.createTransition
	}
	
	override exportContents(TypeDeclaration source) {
		result.name = source.name.identifier
		result.source = fetchElement(SharedUtils.obtainTypeLiteralAnnotation(source, From))
		result.target = fetchElement(SharedUtils.obtainTypeLiteralAnnotation(source, To))
	}
	
	override tryStore(Element contained) {
		false
	}
	
}