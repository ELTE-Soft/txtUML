package hu.elte.txtuml.layout.export.elementinfo.impl;

import hu.elte.txtuml.layout.export.DiagramType;
import hu.elte.txtuml.layout.export.elementinfo.ElementType;
import hu.elte.txtuml.layout.export.elementinfo.LinkInfo;
import hu.elte.txtuml.layout.export.elementinfo.NodeInfo;
import hu.elte.txtuml.layout.lang.elements.LayoutElement;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;

/**
 * Default implementation for {@link LinkInfo}.
 * 
 * @author Gábor Ferenc Kovács
 *
 */
public class LinkInfoImpl extends ElementInfoImpl implements LinkInfo {

	private final NodeInfo start;
	private final NodeInfo end;
	
	public LinkInfoImpl(Class<? extends LayoutElement> elementClass,
			DiagramType diagType, String asString, NodeInfo start, NodeInfo end) {

		super(elementClass, diagType, asString);
		this.start = start;
		this.end = end;
	}

	@Override
	public ElementType getType() {
		return ElementType.Link;
	}

	@Override
	public LinkInfo asLinkInfo() {
		return this;
	}

	@Override
	public LineAssociation convert() {
		return new LineAssociation(toString(), start.toString(), end.toString());
	}

}
