package hu.elte.txtuml.layout.export.interfaces;

import java.util.Map;

import hu.elte.txtuml.api.layout.elements.LayoutNodeGroup;
import hu.elte.txtuml.layout.export.elementinfo.NodeGroupInfo;
import hu.elte.txtuml.layout.export.impl.NodeGroupMapImpl;

/**
 * 
 * @author Dávid János Németh
 *
 */
public interface NodeGroupMap extends Map<Class<? extends LayoutNodeGroup>, NodeGroupInfo> {
    
    static NodeGroupMap create() {
        return new NodeGroupMapImpl();
    }
    
}