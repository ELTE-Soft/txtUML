package hu.elte.txtuml.export.uml2.structural

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.Exporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.uml2.uml.Signal

class SignalExporter extends Exporter<TypeDeclaration, ITypeBinding, Signal> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(ITypeBinding access) {
		if(ElementTypeTeller.isSignal(access)) factory.createSignal
	}

	override exportContents(TypeDeclaration source) {
		result.name = source.name.identifier
		source.resolveBinding.declaredFields.forEach[exportField[result.ownedAttributes += it]]
	}

}