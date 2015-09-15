package hu.elte.txtuml.layout.export.elementinfo;

import hu.elte.txtuml.layout.export.DiagramType;
import hu.elte.txtuml.layout.export.elementinfo.impl.LinkGroupInfoImpl;
import hu.elte.txtuml.layout.export.interfaces.LinkMap;

/**
 * Information holder about a link group in a diagram layout description.
 * 
 * @author Dávid János Németh
 *
 */
public interface LinkGroupInfo extends GroupInfo {

    static LinkGroupInfo create(Class<?> elementClass, DiagramType diagType, String asString) {
        return new LinkGroupInfoImpl(elementClass, diagType, asString);
    }
    
    LinkMap getAllLinks();
    
    void addLink(LinkInfo link);
    
}
