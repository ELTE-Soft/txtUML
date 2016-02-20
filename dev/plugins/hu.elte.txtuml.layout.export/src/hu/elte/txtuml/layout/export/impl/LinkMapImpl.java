package hu.elte.txtuml.layout.export.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import hu.elte.txtuml.layout.export.elementinfo.LinkInfo;
import hu.elte.txtuml.layout.export.interfaces.LinkMap;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;

/**
 * Default implementation for {@link LinkMap}.
 */
@SuppressWarnings("serial")
public class LinkMapImpl extends HashMap<Class<?>, LinkInfo> implements LinkMap {

	@Override
	public Set<LineAssociation> convert() {
		Set<LineAssociation> set = new HashSet<>();
		this.values().forEach( v -> {
			set.add(v.convert());
		});
		
		return set;
	}

}
