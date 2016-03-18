package hu.elte.txtuml.export.uml2.restructured

import org.eclipse.uml2.uml.Element

abstract class UniformExporter<S, R extends Element> extends Exporter<S, S, R> {
	
	new() {
	}
	
	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override createFetched(S a) { create(a) }
	
}