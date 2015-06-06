package hu.elte.txtuml.layout.export.interfaces;

import hu.elte.txtuml.layout.export.elementinfo.NodeInfo;
import hu.elte.txtuml.layout.export.impl.NodeMapImpl;
import hu.elte.txtuml.layout.lang.elements.LayoutNode;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Gábor Ferenc Kovács
 *
 */
public interface NodeMap extends Map<Class<? extends LayoutNode>, NodeInfo> {

	static NodeMap create() {
		return new NodeMapImpl();
	}
	
	Set<RectangleObject> convert();
	
}
