package hu.elte.txtuml.layout.export.elementinfo;

import hu.elte.txtuml.layout.export.DiagramType;
import hu.elte.txtuml.layout.export.elementinfo.impl.GeneralizationLinkInfoImpl;
import hu.elte.txtuml.layout.export.elementinfo.impl.LinkInfoImpl;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;

/**
 * Information holder about a link in a diagram layout description.
 * 
 * @author Gabor Ferenc Kovacs
 *
 */
public interface LinkInfo extends ConcreteElementInfo {

	static LinkInfo create(Class<?> elementClass,
			DiagramType diagType, String asString, NodeInfo start, NodeInfo end) {
		
		return new LinkInfoImpl(elementClass, diagType, asString, start, end);
	}
	
	static LinkInfo createGeneralization(Class<?> elementClass,
            String asString, NodeInfo start, NodeInfo end) {
	    
	    return new GeneralizationLinkInfoImpl(elementClass, DiagramType.Class, asString, start, end);
	}

	/**
	 * @return The <code>LineAssociation</code> representation of the link this object holds info about.
	 */
	LineAssociation convert();

}
