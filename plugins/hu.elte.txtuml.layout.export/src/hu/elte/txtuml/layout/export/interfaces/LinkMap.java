package hu.elte.txtuml.layout.export.interfaces;

import java.util.Map;
import java.util.Set;

import hu.elte.txtuml.layout.export.elementinfo.LinkInfo;
import hu.elte.txtuml.layout.export.impl.LinkMapImpl;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;

/**
 * 
 * @author Gabor Ferenc Kovacs
 *
 */
public interface LinkMap extends Map<Class<?>, LinkInfo> {
	
	static LinkMap create() {
		return new LinkMapImpl();
	}
	
	Set<LineAssociation> convert();
	
}
