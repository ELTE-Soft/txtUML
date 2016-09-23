package hu.elte.txtuml.export.papyrus.diagrams.clazz.impl;

import java.util.Collection;
import java.util.Map;

import org.eclipse.uml2.uml.Element;

import hu.elte.txtuml.export.papyrus.layout.IDiagramElementsMapper;

public class ClassDiagramElementsMapper implements IDiagramElementsMapper{

	Map<String, Element> connectionMap;
	Map<String, Element> elementMap;
	
	public ClassDiagramElementsMapper(Map<String, Element> elementMap, Map<String, Element> connectionMap) {
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
