package hu.elte.txtuml.export.uml2.structural

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.Exporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.uml2.uml.Signal
import org.eclipse.uml2.uml.SignalEvent

class SignalEventExporter extends Exporter<TypeDeclaration, ITypeBinding, SignalEvent> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(ITypeBinding access) {
		if(ElementTypeTeller.isSignal(access)) factory.createSignalEvent
	}

	override exportContents(TypeDeclaration source) {
		result.name = source.name.identifier
		result.signal = fetchElement(source.resolveBinding, new SignalExporter(this)) as Signal
	}

}