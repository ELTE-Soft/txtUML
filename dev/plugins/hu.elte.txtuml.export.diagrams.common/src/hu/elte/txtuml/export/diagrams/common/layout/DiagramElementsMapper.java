package hu.elte.txtuml.export.diagrams.common.layout;

import java.util.Collection;
import java.util.Map;

import org.eclipse.uml2.uml.Element;

public class DiagramElementsMapper implements IDiagramElementsMapper{

	Map<String, Element> connectionMap;
	Map<String, Element> elementMap;
	
	public DiagramElementsMapper(Map<String, Element> elementMap, Map<String, Element> connectionMap) {
		this.connectionMap = connectionMap;
		this.elementMap = elementMap;
	}
	
	@Override
	public Element findNode(String object) {
		return this.elementMap.get(object);
	}

	@Override
	public Element findConnection(String object) {
		return this.connectionMap.get(object);
	}

	@Override
	public Collection<Element> getNodes() {
		return this.elementMap.values();
	}
	
	@Override
	public Collection<Element> getConnections() {
		return this.connectionMap.values();
	}
}