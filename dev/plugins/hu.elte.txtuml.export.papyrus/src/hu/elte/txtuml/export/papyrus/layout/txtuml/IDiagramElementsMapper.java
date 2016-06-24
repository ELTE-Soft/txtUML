package hu.elte.txtuml.export.papyrus.layout.txtuml;

import java.util.Collection;

import org.eclipse.uml2.uml.Element;

public interface IDiagramElementsMapper {
	Element findNode(String object);

	Element findConnection(String object);

	Collection<Element> getNodes();

	Collection<Element> getConnections();
}
