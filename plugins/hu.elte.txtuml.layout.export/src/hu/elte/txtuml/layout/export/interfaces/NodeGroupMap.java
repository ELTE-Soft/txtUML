package hu.elte.txtuml.layout.export.interfaces;

import java.util.Map;

import hu.elte.txtuml.layout.export.elementinfo.NodeGroupInfo;
import hu.elte.txtuml.layout.export.impl.NodeGroupMapImpl;

/**
 * 
 * @author D�vid J�nos N�meth
 *
 */
public interface NodeGroupMap extends Map<Class<?>, NodeGroupInfo> {
    
    static NodeGroupMap create() {
        return new NodeGroupMapImpl();
    }
    
}