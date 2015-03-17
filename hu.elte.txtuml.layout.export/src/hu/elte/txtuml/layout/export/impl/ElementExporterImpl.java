package hu.elte.txtuml.layout.export.impl;

import hu.elte.txtuml.layout.export.DiagramType;
import hu.elte.txtuml.layout.export.diagramexporters.ClassDiagramExporter;
import hu.elte.txtuml.layout.export.elementinfo.ElementInfo;
import hu.elte.txtuml.layout.export.elementinfo.LinkInfo;
import hu.elte.txtuml.layout.export.elementinfo.NodeInfo;
import hu.elte.txtuml.layout.export.interfaces.ElementExporter;
import hu.elte.txtuml.layout.export.interfaces.LinkMap;
import hu.elte.txtuml.layout.export.interfaces.NodeMap;
import hu.elte.txtuml.layout.lang.elements.LayoutElement;
import hu.elte.txtuml.layout.lang.elements.LayoutLink;
import hu.elte.txtuml.layout.lang.elements.LayoutNode;
import hu.elte.txtuml.layout.lang.elements.LayoutNonGroupElement;
import hu.elte.txtuml.utils.Pair;

/**
 * Default implementation for {@link ElementExporter}.
 * 
 * @author Gábor Ferenc Kovács
 *
 */
public class ElementExporterImpl implements ElementExporter {

	public DiagramType type;
	public final NodeMap nodes;
	public final LinkMap links;

	public ElementExporterImpl(NodeMap nodes, LinkMap links) {
		this(nodes, links, DiagramType.Unknown);
	}
	
	public ElementExporterImpl(NodeMap nodes, LinkMap links, DiagramType type) {
		this.nodes = nodes;
		this.links = links;
		this.type = type;
	}
	
	@Override
	public ElementInfo exportElement(Class<? extends LayoutElement> element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ElementInfo exportNonGroupElement(
			Class<? extends LayoutNonGroupElement> elementClass) {

		ElementInfo info;

		info = nodes.get(elementClass);
		if (info != null) {
			return info;
		}

		info = links.get(elementClass);
		if (info != null) {
			return info;
		}

		info = exportNewNode(elementClass);
		if (info != null) {
			return info;
		}

		info = exportNewLink(elementClass);
		if (info != null) {
			return info;
		}

		return ElementInfo.createInvalid(elementClass);
	}

	@Override
	public ElementInfo exportNode(Class<? extends LayoutNode> nodeClass) {
		ElementInfo info;

		info = nodes.get(nodeClass);
		if (info != null) {
			return info;
		}
		
		info = exportNewNode(nodeClass);
		if (info != null) {
			return info;
		}

		return ElementInfo.createInvalid(nodeClass);
	}

	@SuppressWarnings("unchecked")
	private NodeInfo exportNewNode(Class<? extends LayoutElement> cls) {
		if (ClassDiagramExporter.isNode(cls)) {
			NodeInfo info = NodeInfo.create(cls, DiagramType.Class, asString(cls));
			nodes.put((Class<? extends LayoutNode>) cls, info);
			return info;
		}
		// TODO add more diag types
		return null;
	}

	@SuppressWarnings("unchecked")
	private LinkInfo exportNewLink(Class<? extends LayoutElement> cls) {
		if (ClassDiagramExporter.isLink(cls)) {
			Pair<Class<? extends LayoutNode>, Class<? extends LayoutNode>> p = ClassDiagramExporter.startAndEndOfLink(cls);
			
			LinkInfo info = LinkInfo.create(cls, DiagramType.Class, asString(cls), exportNode(p.getKey()).asNodeInfo(), exportNode(p.getValue()).asNodeInfo());
			links.put((Class<? extends LayoutLink>) cls, info);
			return info;
		}
		// TODO add more diag types
		return null;
	}
	
	private String asString(Class<?> cls) {
		return cls.getCanonicalName();
	}
	
	
}
