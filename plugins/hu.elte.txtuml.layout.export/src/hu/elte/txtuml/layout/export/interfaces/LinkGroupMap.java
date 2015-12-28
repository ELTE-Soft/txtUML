package hu.elte.txtuml.layout.export.interfaces;

import hu.elte.txtuml.layout.export.elementinfo.LinkGroupInfo;
import hu.elte.txtuml.layout.export.impl.LinkGroupMapImpl;

import java.util.Map;

/**
 * 
 * @author Dávid János Németh
 *
 */
public interface LinkGroupMap extends Map<Class<?>, LinkGroupInfo> {
    
    static LinkGroupMap create() {
        return new LinkGroupMapImpl();
    }
    
}