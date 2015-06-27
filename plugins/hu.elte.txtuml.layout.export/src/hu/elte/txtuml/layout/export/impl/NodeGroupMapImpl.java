package hu.elte.txtuml.layout.export.impl;

import java.util.HashMap;

import hu.elte.txtuml.layout.export.elementinfo.NodeGroupInfo;
import hu.elte.txtuml.layout.export.interfaces.NodeGroupMap;
import hu.elte.txtuml.layout.lang.elements.LayoutNodeGroup;

/**
 * 
 * @author Dávid János Németh
 *
 */
@SuppressWarnings("serial")
public class NodeGroupMapImpl extends HashMap<Class<? extends LayoutNodeGroup>, NodeGroupInfo> implements NodeGroupMap {
    
}
