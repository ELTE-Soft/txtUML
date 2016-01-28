package hu.elte.txtuml.layout.export.elementinfo.impl;

import hu.elte.txtuml.layout.export.elementinfo.LinkInfo;
import hu.elte.txtuml.layout.export.elementinfo.NodeInfo;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;

/**
 * Default implementation for {@link LinkInfo}.
 */
public class LinkInfoImpl extends ElementInfoImpl implements LinkInfo {

	protected final NodeInfo start;
	protected final NodeInfo end;

	public LinkInfoImpl(Class<?> elementClass, String asString, NodeInfo start,
			NodeInfo end) {
		super(elementClass, asString);
		this.start = start;
		this.end = end;
	}

	@Override
	public LineAssociation convert() {
		return new LineAssociation(toString(), start.toString(), end.toString());
	}

}
