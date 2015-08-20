package hu.elte.txtuml.layout.export.elementinfo;

import hu.elte.txtuml.api.layout.AlignmentType;
import hu.elte.txtuml.api.layout.elements.LayoutElement;
import hu.elte.txtuml.layout.export.DiagramType;
import hu.elte.txtuml.layout.export.interfaces.NodeMap;
import hu.elte.txtuml.layout.export.elementinfo.NodeInfo;
import hu.elte.txtuml.layout.export.elementinfo.impl.NodeGroupInfoImpl;

/**
 * 
 * @author Dávid János Németh
 *
 */
public interface NodeGroupInfo extends ElementInfo {

    static NodeGroupInfo create(Class<? extends LayoutElement> elementClass, DiagramType diagType, String asString) {
        return new NodeGroupInfoImpl(elementClass, diagType, asString);
    }
    
    boolean beingExported();
    
    void setBeingExported(boolean val);
    
    NodeMap getAllNodes();
    
    void addNode(NodeInfo node);
    
    void setAlignment(AlignmentType alignment);
    
    AlignmentType getAlignment();
    
}
