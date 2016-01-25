package hu.elte.txtuml.layout.export.interfaces;

import java.util.Map;

import hu.elte.txtuml.layout.export.elementinfo.LinkGroupInfo;
import hu.elte.txtuml.layout.export.impl.LinkGroupMapImpl;

/**
 * 
 * @author D�vid J�nos N�meth
 *
 */
public interface LinkGroupMap extends Map<Class<?>, LinkGroupInfo> {
    
    static LinkGroupMap create() {
        return new LinkGroupMapImpl();
    }
    
}