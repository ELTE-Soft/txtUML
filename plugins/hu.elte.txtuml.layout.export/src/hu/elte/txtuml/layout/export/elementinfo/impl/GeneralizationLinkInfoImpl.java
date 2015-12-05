package hu.elte.txtuml.layout.export.elementinfo.impl;

import hu.elte.txtuml.layout.export.elementinfo.LinkInfo;
import hu.elte.txtuml.layout.export.elementinfo.NodeInfo;
import hu.elte.txtuml.layout.visualizer.model.AssociationType;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;

/**
 * Implementation for {@link LinkInfo} which contains information about a
 * generalization link.
 * 
 * @author Gabor Ferenc Kovacs
 *
 */
public class GeneralizationLinkInfoImpl extends LinkInfoImpl implements
		LinkInfo {

	public GeneralizationLinkInfoImpl(Class<?> elementClass, String asString,
			NodeInfo start, NodeInfo end) {
		super(elementClass, asString, start, end);
	}

	@Override
	public LineAssociation convert() {
		return new LineAssociation(toString(), start.toString(),
				end.toString(), AssociationType.generalization);
	}

}
