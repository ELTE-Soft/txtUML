package hu.elte.txtuml.export.uml2.stdlib

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.Exporter
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.uml2.uml.Class

class StdlibClassExporter extends Exporter<Void, ITypeBinding, Class> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(ITypeBinding access) {
		if (access.qualifiedName.startsWith("hu.elte.txtuml.api.stdlib."))
			getImportedElement(access.name) as Class
	}

	override exportContents(Void source) {
		throw new UnsupportedOperationException("Timer contents should not be exported")
	}

}