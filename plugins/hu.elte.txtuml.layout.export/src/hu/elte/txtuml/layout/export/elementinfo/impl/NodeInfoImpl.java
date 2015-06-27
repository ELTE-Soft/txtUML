package hu.elte.txtuml.layout.export.elementinfo.impl;

import hu.elte.txtuml.layout.export.DiagramType;
import hu.elte.txtuml.layout.export.elementinfo.ElementType;
import hu.elte.txtuml.layout.export.elementinfo.NodeInfo;
import hu.elte.txtuml.layout.lang.elements.LayoutElement;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

/**
 * Default implementation for {@link NodeInfo}.
 * 
 * @author Gábor Ferenc Kovács
 *
 */
public class NodeInfoImpl extends ElementInfoImpl implements NodeInfo {

	public NodeInfoImpl(Class<? extends LayoutElement> elementClass, DiagramType diagType, String asString) {
		super(elementClass, diagType, asString);
	}

	@Override
	public ElementType getType() {
		return ElementType.Node;
	}

	@Override
	public NodeInfo asNodeInfo() {
		return this;
	}

	@Override
	public RectangleObject convert() {
		return new RectangleObject(toString());
	}
	
	@Override
	public boolean isPhantom() {
	    return toString().startsWith("#phantom_");
	}

}
