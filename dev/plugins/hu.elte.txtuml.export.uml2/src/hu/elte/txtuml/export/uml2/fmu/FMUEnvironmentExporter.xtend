package hu.elte.txtuml.export.uml2.fmu

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.Exporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.uml2.uml.Class

class FMUEnvironmentExporter extends Exporter<TypeDeclaration, ITypeBinding, Class> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(ITypeBinding typ) {
		if(ElementTypeTeller.isFMUEnvironment(typ)) getImportedElement("FMUEnvironment") as Class
	}

	override exportContents(TypeDeclaration typeDecl) {
	}

}