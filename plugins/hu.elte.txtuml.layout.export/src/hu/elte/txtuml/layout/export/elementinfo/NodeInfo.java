package hu.elte.txtuml.layout.export.elementinfo;

import hu.elte.txtuml.api.layout.elements.LayoutElement;
import hu.elte.txtuml.layout.export.DiagramType;
import hu.elte.txtuml.layout.export.elementinfo.impl.NodeInfoImpl;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

/**
 * Information holder about a node in a diagram layout description.
 * 
 * @author Gábor Ferenc Kovács
 *
 */
public interface NodeInfo extends ElementInfo {

	/**
	 * Creates new instance.
	 * 
	 * @return The new instance.
	 */
	static NodeInfo create(Class<? extends LayoutElement> elementClass, DiagramType diagType, String asString) {	
		return new NodeInfoImpl(elementClass, diagType, asString);
	}
	
	/**
	 * @return The <code>RectangleObject</code> representation of the node this object holds info about.
	 */
	RectangleObject convert();
	
	boolean isPhantom();
	
}
