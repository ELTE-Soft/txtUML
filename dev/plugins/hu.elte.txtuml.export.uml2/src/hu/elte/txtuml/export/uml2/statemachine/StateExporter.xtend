package hu.elte.txtuml.export.uml2.statemachine

import hu.elte.txtuml.export.uml2.Exporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.uml2.uml.Pseudostate
import org.eclipse.uml2.uml.PseudostateKind
import org.eclipse.uml2.uml.State
import org.eclipse.uml2.uml.Vertex
import org.eclipse.jdt.core.dom.MethodDeclaration

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
		if(ElementTypeTeller.isState(access)) factory.createState
	}
	
	override exportContents(TypeDeclaration source) {
		super.exportContents(source)
		source.bodyDeclarations.filter[it instanceof MethodDeclaration].filter [
			(it as MethodDeclaration).name.identifier == "entry"
		].forEach[exportSMActivity(it as MethodDeclaration)[ result.entry = it ]]
		source.bodyDeclarations.filter[it instanceof MethodDeclaration].filter [
			(it as MethodDeclaration).name.identifier == "exit"
		].forEach[exportSMActivity(it as MethodDeclaration)[ result.exit = it ]]

	}
	
}

class InitStateExporter extends AbstractStateExporter<Pseudostate> {

	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(ITypeBinding access) {
		if(ElementTypeTeller.isInitialPseudoState(access)) factory.createPseudostate
	}

	override exportContents(TypeDeclaration source) {
		result.kind = PseudostateKind.INITIAL_LITERAL
		super.exportContents(source)
	}
}