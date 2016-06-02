package hu.elte.txtuml.export.uml2.stdlib

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.Exporter
import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.uml2.uml.Class
import org.eclipse.uml2.uml.Operation

class StdlibCallExporter extends Exporter<Void, IMethodBinding, Operation> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(IMethodBinding access) {
		if (access.declaringClass.qualifiedName.startsWith("hu.elte.txtuml.api.stdlib")) {
			val ops = (fetchType(access.declaringClass) as Class).ownedOperations
			ops.findFirst[ name == access.name ]
		}
	}

	override exportContents(Void source) {
		throw new UnsupportedOperationException("Timer contents should not be exported")
	}

}