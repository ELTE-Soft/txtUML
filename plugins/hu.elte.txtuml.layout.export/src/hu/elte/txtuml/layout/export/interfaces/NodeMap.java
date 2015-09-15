package hu.elte.txtuml.layout.export.interfaces;

import hu.elte.txtuml.layout.export.elementinfo.NodeInfo;
import hu.elte.txtuml.layout.export.impl.NodeMapImpl;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Gabor Ferenc Kovacs
 *
 */
public interface NodeMap extends Map<Class<?>, NodeInfo> {

	static NodeMap create() {
		return new NodeMapImpl();
	}
	
	Set<RectangleObject> convert();
	
}
