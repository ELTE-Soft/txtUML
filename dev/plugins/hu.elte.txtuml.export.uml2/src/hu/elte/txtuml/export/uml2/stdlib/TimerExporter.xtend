package hu.elte.txtuml.export.uml2.stdlib

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.Exporter
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.uml2.uml.Class
import hu.elte.txtuml.api.stdlib.timers.Timer

class TimerExporter extends Exporter<Void, ITypeBinding, Class> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(ITypeBinding access) {
		if (access.qualifiedName == Timer.canonicalName)
			getImportedElement("Timer") as Class
	}

	override exportContents(Void source) {
		throw new UnsupportedOperationException("Timer contents should not be exported")
	}

}