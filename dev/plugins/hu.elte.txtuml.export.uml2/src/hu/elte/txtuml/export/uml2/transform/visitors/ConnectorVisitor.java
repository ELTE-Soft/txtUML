package hu.elte.txtuml.export.uml2.transform.visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import hu.elte.txtuml.export.uml2.transform.exporters.ConnectorExporter;
import hu.elte.txtuml.utils.jdt.ElementTypeTeller;

public class ConnectorVisitor extends ASTVisitor {

	private final ConnectorExporter connectorExporter;

	public ConnectorVisitor(ConnectorExporter connectorExporter) {
		this.connectorExporter = connectorExporter;
	}

	@Override
	public boolean visit(TypeDeclaration typeDeclaration) {
		if (ElementTypeTeller.isConnector(typeDeclaration)) {
			connectorExporter.exportPort(typeDeclaration);
		}
		return true;
	}
}
