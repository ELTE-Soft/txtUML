package hu.elte.txtuml.layout.export.elementinfo.impl;

import hu.elte.txtuml.api.layout.elements.LayoutElement;
import hu.elte.txtuml.layout.export.DiagramType;
import hu.elte.txtuml.layout.export.elementinfo.ElementType;
import hu.elte.txtuml.layout.export.elementinfo.LinkInfo;
import hu.elte.txtuml.layout.export.elementinfo.NodeInfo;
import hu.elte.txtuml.layout.visualizer.model.AssociationType;
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
	private final boolean isGeneralization;
	
	public LinkInfoImpl(Class<? extends LayoutElement> elementClass,
			DiagramType diagType, String asString, NodeInfo start, NodeInfo end, boolean isGeneralization) {

		super(elementClass, diagType, asString);
		this.start = start;
		this.end = end;
		this.isGeneralization = isGeneralization;
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
	    if (isGeneralization) {
	        return new LineAssociation(toString(), start.toString(), end.toString(), AssociationType.generalization);
	    } else {
	        return new LineAssociation(toString(), start.toString(), end.toString());
	    }
	}

}
