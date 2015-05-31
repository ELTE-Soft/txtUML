package hu.elte.txtuml.layout.export.elementinfo;

import hu.elte.txtuml.layout.export.DiagramType;
import hu.elte.txtuml.layout.export.elementinfo.impl.LinkInfoImpl;
import hu.elte.txtuml.layout.lang.elements.LayoutElement;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;

/**
 * Information holder about a link in a diagram layout description.
 * 
 * @author Gábor Ferenc Kovács
 *
 */
public interface LinkInfo extends ElementInfo {

	/**
	 * Creates new instance.
	 * 
	 * @return The new instance.
	 */
	static LinkInfo create(Class<? extends LayoutElement> elementClass,
			DiagramType diagType, String asString, NodeInfo start, NodeInfo end) {
		
		return new LinkInfoImpl(elementClass, diagType, asString, start, end);
	}

	/**
	 * @return The <code>LineAssociation</code> representation of the link this object holds info about.
	 */
	LineAssociation convert();

}
