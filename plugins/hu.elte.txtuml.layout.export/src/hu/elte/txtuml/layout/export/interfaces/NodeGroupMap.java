package hu.elte.txtuml.layout.export.interfaces;

import hu.elte.txtuml.layout.export.elementinfo.NodeGroupInfo;
import hu.elte.txtuml.layout.export.impl.NodeGroupMapImpl;

import java.util.Map;

/**
 * 
 * @author Dávid János Németh
 *
 */
public interface NodeGroupMap extends Map<Class<?>, NodeGroupInfo> {
    
    static NodeGroupMap create() {
        return new NodeGroupMapImpl();
    }
    
}