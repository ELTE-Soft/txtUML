package hu.elte.txtuml.export.uml2.restructured.statemachine

import hu.elte.txtuml.export.uml2.restructured.Exporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.uml2.uml.Element
import org.eclipse.uml2.uml.State
import org.eclipse.uml2.uml.Pseudostate
import org.eclipse.uml2.uml.PseudostateKind
import org.eclipse.uml2.uml.Vertex

abstract class AbstractStateExporter<V extends Vertex> extends Exporter<TypeDeclaration, ITypeBinding, V> {
	
	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override exportContents(TypeDeclaration source) {
		result.name = source.name.identifier
	}
	
}

class StateExporter extends AbstractStateExporter<State> {
	
	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(ITypeBinding access) {
		if (ElementTypeTeller.isState(access)) factory.createState
	}
	
	override tryStore(Element contained) {
		false
	}
}

class InitStateExporter extends AbstractStateExporter<Pseudostate> {
	
	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(ITypeBinding access) {
		if (ElementTypeTeller.isInitialPseudoState(access)) factory.createPseudostate
	}
	
	override exportContents(TypeDeclaration source) {
		result.kind = PseudostateKind.INITIAL_LITERAL
		super.exportContents(source)
	}
	
	override tryStore(Element contained) {
		false
	}
}