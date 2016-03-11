package hu.elte.txtuml.export.uml2.transform.exporters;

import java.util.List;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Connector;
import org.eclipse.uml2.uml.ConnectorEnd;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Port;

import hu.elte.txtuml.export.uml2.utils.MultiplicityProvider;

public class ConnectorExporter {

	private TypeExporter typeExporter;
	private List<Port> ports;
	private Model exportedModel;

	public ConnectorExporter(Model exportedModel, List<Port> ports, TypeExporter typeExporter) {
		this.exportedModel = exportedModel;
		this.ports = ports;
		this.typeExporter = typeExporter;
	}

	public void exportPort(TypeDeclaration typeDeclaration) {
		List<?> classes = typeDeclaration.bodyDeclarations();

		String connectorName = typeDeclaration.getName().getIdentifier();
		Class connectorWrapper = exportedModel.createOwnedClass(connectorName + "_wrapper", true);

		Connector connector = connectorWrapper.createOwnedConnector(connectorName);

		createEnd(connector, (TypeDeclaration) classes.get(0));
		createEnd(connector, (TypeDeclaration) classes.get(1));
	}

	private void createEnd(Connector connector, TypeDeclaration typeDeclaration) {
		ITypeBinding[] typeArguments = typeDeclaration.resolveBinding().getSuperclass().getTypeArguments();
		
		ConnectorEnd end = connector.createEnd();
		for (Port port : ports) {
			if (port.getName().equals(typeArguments[1].getName())) {
				end.setRole(port);
				break;
			}
		}
		
		end.setLower(MultiplicityProvider.getLowerBound(typeDeclaration));
		end.setUpper(MultiplicityProvider.getUpperBound(typeDeclaration));
	}

}
