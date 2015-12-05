package hu.elte.txtuml.layout.export.impl;

import hu.elte.txtuml.layout.export.elementinfo.NodeInfo;
import hu.elte.txtuml.layout.export.interfaces.NodeMap;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Default implementation for {@link NodeMap}.
 * 
 * @author Gabor Ferenc Kovacs
 *
 */
@SuppressWarnings("serial")
public class NodeMapImpl extends LinkedHashMap<Class<?>, NodeInfo> implements NodeMap {

	@Override
	public Set<RectangleObject> convert() {
		Set<RectangleObject> set = new HashSet<>();
		this.values().forEach( v -> {
		    if (!v.isPhantom()) {
		        set.add(v.convert());
		    }
		});

		return set;
	}

}
