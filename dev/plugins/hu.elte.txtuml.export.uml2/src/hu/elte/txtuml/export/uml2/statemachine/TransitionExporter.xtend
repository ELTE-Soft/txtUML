package hu.elte.txtuml.export.uml2.statemachine

import hu.elte.txtuml.api.model.From
import hu.elte.txtuml.api.model.To
import hu.elte.txtuml.api.model.Trigger
import hu.elte.txtuml.export.uml2.Exporter
import hu.elte.txtuml.export.uml2.structural.SignalEventExporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import hu.elte.txtuml.utils.jdt.SharedUtils
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.uml2.uml.SignalEvent
import org.eclipse.uml2.uml.Transition
import org.eclipse.uml2.uml.Vertex

class TransitionExporter extends Exporter<TypeDeclaration, ITypeBinding, Transition> {

	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(ITypeBinding access) {
		if(ElementTypeTeller.isTransition(access)) factory.createTransition
	}

	override exportContents(TypeDeclaration source) {
		result.name = source.name.identifier
		result.source = fetchElement(SharedUtils.obtainTypeLiteralAnnotation(source, From)) as Vertex
		result.target = fetchElement(SharedUtils.obtainTypeLiteralAnnotation(source, To)) as Vertex

		val triggerSignal = SharedUtils.obtainTypeLiteralAnnotation(source, Trigger)
		if (triggerSignal != null) {
			val sigEvent = fetchElement(triggerSignal, new SignalEventExporter(this)) as SignalEvent
			val trigger = result.createTrigger(sigEvent.name + "_trigger")
			trigger.event = sigEvent
		}

		if (exportActions) {
			source.bodyDeclarations.filter[it instanceof MethodDeclaration].filter [
				(it as MethodDeclaration).name.identifier == "effect"
			].forEach[exportSMActivity(it as MethodDeclaration)[result.effect = it]]

			source.bodyDeclarations.filter[it instanceof MethodDeclaration].map[it as MethodDeclaration].filter [
				name.identifier == "guard"
			].forEach[exportGuard[result.guard = it]]
		}
	}
}