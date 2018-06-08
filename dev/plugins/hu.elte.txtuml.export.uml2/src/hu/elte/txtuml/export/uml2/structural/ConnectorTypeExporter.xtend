package hu.elte.txtuml.export.uml2.structural

import hu.elte.txtuml.export.uml2.Exporter
import org.eclipse.uml2.uml.Association
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.TypeDeclaration
import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller

class ConnectorTypeExporter extends Exporter<TypeDeclaration, ITypeBinding, Association> {
	
		new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(ITypeBinding access) {
		if (ElementTypeTeller.isConnector(access)) factory.createAssociation
	}
	
	override exportContents(TypeDeclaration source) {
		source.types.forEach[exportConnectorTypeEnd[result.ownedEnds += it]]
		result.name = source.name.identifier
	}
	
}