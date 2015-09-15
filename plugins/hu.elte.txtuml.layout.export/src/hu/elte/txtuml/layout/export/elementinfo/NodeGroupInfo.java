package hu.elte.txtuml.layout.export.elementinfo;

import hu.elte.txtuml.api.layout.AlignmentType;
import hu.elte.txtuml.layout.export.DiagramType;
import hu.elte.txtuml.layout.export.elementinfo.impl.NodeGroupInfoImpl;
import hu.elte.txtuml.layout.export.interfaces.NodeMap;

/**
 * Information holder about a node group in a diagram layout description.
 * 
 * @author Dávid János Németh
 *
 */
public interface NodeGroupInfo extends GroupInfo {

    static NodeGroupInfo create(Class<?> elementClass, DiagramType diagType, String asString) {
        return new NodeGroupInfoImpl(elementClass, diagType, asString);
    }
    
    NodeMap getAllNodes();
    
    void addNode(NodeInfo node);
    
    void setAlignment(AlignmentType alignment);
    
    AlignmentType getAlignment();
    
}
