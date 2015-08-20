package hu.elte.txtuml.layout.export.interfaces;

import hu.elte.txtuml.api.layout.elements.LayoutLink;
import hu.elte.txtuml.layout.export.elementinfo.LinkInfo;
import hu.elte.txtuml.layout.export.impl.LinkMapImpl;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;

import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Gábor Ferenc Kovács
 *
 */
public interface LinkMap extends Map<Class<? extends LayoutLink>, LinkInfo> {
	
	static LinkMap create() {
		return new LinkMapImpl();
	}
	
	Set<LineAssociation> convert();
	
}
