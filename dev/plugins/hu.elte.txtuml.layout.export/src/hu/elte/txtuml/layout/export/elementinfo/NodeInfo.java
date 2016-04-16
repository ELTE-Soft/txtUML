package hu.elte.txtuml.layout.export.elementinfo;

import hu.elte.txtuml.layout.export.elementinfo.impl.NodeInfoImpl;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.model.SpecialBox;

/**
 * Information holder about a node in a diagram layout description.
 */
public interface NodeInfo extends ConcreteElementInfo {

	static NodeInfo create(Class<?> elementClass, String asString) {
		return new NodeInfoImpl(elementClass, asString);
	}

	/**
	 * @return The <code>RectangleObject</code> representation of the node this
	 *         object holds info about.
	 */
	RectangleObject convert();

	boolean isPhantom();
	
	SpecialBox getSpecialProperty();

}
