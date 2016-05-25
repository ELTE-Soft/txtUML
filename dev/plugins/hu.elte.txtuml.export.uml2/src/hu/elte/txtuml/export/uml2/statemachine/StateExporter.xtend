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
import org.eclipse.uml2.uml.Element
import org.eclipse.uml2.uml.Transition

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
		if (ElementTypeTeller.isCompositeState(source)) {
			result.createRegion(source.name.identifier)
			source.bodyDeclarations.filter[it instanceof TypeDeclaration].forEach[
				exportElement(it, (it as TypeDeclaration).resolveBinding)[storeSMElement]
			]
		}
		if (exportActions) {
			source.bodyDeclarations.filter[it instanceof MethodDeclaration].filter [
				(it as MethodDeclaration).name.identifier == "entry"
			].forEach[exportSMActivity(it as MethodDeclaration)[result.entry = it]]
			source.bodyDeclarations.filter[it instanceof MethodDeclaration].filter [
				(it as MethodDeclaration).name.identifier == "exit"
			].forEach[exportSMActivity(it as MethodDeclaration)[result.exit = it]]
		}
	}

	def storeSMElement(Element contained) {
		switch contained {
			Vertex: result.regions.get(0).subvertices.add(contained)
			Transition: result.regions.get(0).transitions.add(contained)
		}
	}
}

class ChoiceStateExporter extends AbstractStateExporter<Pseudostate> {

	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(ITypeBinding access) {
		if(ElementTypeTeller.isChoicePseudoState(access)) factory.createPseudostate
	}

	override exportContents(TypeDeclaration source) {
		result.kind = PseudostateKind.CHOICE_LITERAL
		super.exportContents(source)
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