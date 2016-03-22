package hu.elte.txtuml.export.uml2.restructured.statemachine

import hu.elte.txtuml.api.model.From
import hu.elte.txtuml.api.model.To
import hu.elte.txtuml.export.uml2.restructured.Exporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import hu.elte.txtuml.utils.jdt.SharedUtils
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.uml2.uml.Transition
import org.eclipse.uml2.uml.Vertex

class TransitionExporter extends Exporter<TypeDeclaration, ITypeBinding, Transition> {
	
	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(ITypeBinding access) {
		if (ElementTypeTeller.isTransition(access)) factory.createTransition
	}
	
	override exportContents(TypeDeclaration source) {
		result.name = source.name.identifier
		result.source = fetchElement(SharedUtils.obtainTypeLiteralAnnotation(source, From)) as Vertex
		result.target = fetchElement(SharedUtils.obtainTypeLiteralAnnotation(source, To)) as Vertex
	}	
}