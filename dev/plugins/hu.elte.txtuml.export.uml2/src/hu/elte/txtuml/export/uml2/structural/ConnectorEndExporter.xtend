package hu.elte.txtuml.export.uml2.structural

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.Exporter
import hu.elte.txtuml.export.uml2.utils.MultiplicityProvider
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.uml2.uml.ConnectorEnd
import org.eclipse.uml2.uml.Property
import org.eclipse.uml2.uml.Port

class ConnectorEndExporter extends Exporter<TypeDeclaration, ITypeBinding, ConnectorEnd> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(ITypeBinding a) {
		if(ElementTypeTeller.isConnectorEnd(a)) {
			// we need this reference to be set in the export of connect
			val connectorEnd = factory.createConnectorEnd
			val property = fetchElement(a.superclass.typeArguments.get(0)) as Property
			connectorEnd.partWithPort = property
			return connectorEnd
		}
	}

	override exportContents(TypeDeclaration decl) {
		result.lower = MultiplicityProvider.getLowerBound(decl);
		result.upper = MultiplicityProvider.getUpperBound(decl);
		
		val port = fetchElement(decl.resolveBinding.superclass.typeArguments.get(1)) as Port
		result.role = port
	}
}