package hu.elte.txtuml.layout.export.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import hu.elte.txtuml.layout.export.elementinfo.LinkInfo;
import hu.elte.txtuml.layout.export.interfaces.LinkMap;
import hu.elte.txtuml.layout.lang.elements.LayoutLink;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;

/**
 * Default implementation for {@link LinkMap}.
 * 
 * @author Gábor Ferenc Kovács
 *
 */
@SuppressWarnings("serial")
public class LinkMapImpl extends HashMap<Class<? extends LayoutLink>, LinkInfo> implements LinkMap {

	@Override
	public Set<LineAssociation> convert() {
		Set<LineAssociation> set = new HashSet<>();
		forEach( (k, v) -> {
			set.add(v.convert());
		});
		
		return set;
	}

}
