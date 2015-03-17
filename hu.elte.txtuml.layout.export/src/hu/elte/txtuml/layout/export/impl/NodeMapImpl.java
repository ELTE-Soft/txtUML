package hu.elte.txtuml.layout.export.impl;

import hu.elte.txtuml.layout.export.elementinfo.NodeInfo;
import hu.elte.txtuml.layout.export.interfaces.NodeMap;
import hu.elte.txtuml.layout.lang.elements.LayoutNode;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Default implementation for {@link NodeMap}.
 * 
 * @author Gábor Ferenc Kovács
 *
 */
@SuppressWarnings("serial")
public class NodeMapImpl extends HashMap<Class<? extends LayoutNode>, NodeInfo> implements NodeMap {

	@Override
	public Set<RectangleObject> convert() {
		Set<RectangleObject> set = new HashSet<>();
		this.forEach( (k, v) -> {
			set.add(v.convert());
		});

		return set;
	}

}
