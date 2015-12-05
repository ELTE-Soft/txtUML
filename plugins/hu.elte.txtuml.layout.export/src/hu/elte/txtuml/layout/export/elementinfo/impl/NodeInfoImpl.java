package hu.elte.txtuml.layout.export.elementinfo.impl;

import hu.elte.txtuml.layout.export.elementinfo.NodeInfo;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

/**
 * Default implementation for {@link NodeInfo}.
 * 
 * @author Gabor Ferenc Kovacs
 *
 */
public class NodeInfoImpl extends ElementInfoImpl implements NodeInfo {

	public NodeInfoImpl(Class<?> elementClass, String asString) {
		super(elementClass, asString);
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
