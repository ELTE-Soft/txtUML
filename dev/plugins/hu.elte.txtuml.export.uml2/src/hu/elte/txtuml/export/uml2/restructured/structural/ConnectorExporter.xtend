package hu.elte.txtuml.export.uml2.restructured.structural

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.export.uml2.restructured.Exporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.uml2.uml.Connector
import org.eclipse.uml2.uml.Class

class ConnectorExporter extends Exporter<TypeDeclaration, ITypeBinding, Connector> {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(ITypeBinding access) {
		if (ElementTypeTeller.isConnector(access)) factory.createConnector
	}
	
	override exportContents(TypeDeclaration source) {
		source.types.forEach[exportConnectorEnd[result.ends += it]]
		result.name = source.name.identifier
	}
	
}

class ConnectorWrapperExporter extends Exporter<TypeDeclaration, ITypeBinding, Class> {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(ITypeBinding access) {
		if (ElementTypeTeller.isConnector(access)) factory.createClass
	}
	
	override exportContents(TypeDeclaration source) {
		exportConnector(source)[result.ownedConnectors += it]
		result.name = '''«source.name.identifier»_wrapper'''
	}
	
}