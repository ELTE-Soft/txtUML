package hu.elte.txtuml.layout.export.impl;

import java.util.HashMap;

import hu.elte.txtuml.api.layout.elements.LayoutNodeGroup;
import hu.elte.txtuml.layout.export.elementinfo.NodeGroupInfo;
import hu.elte.txtuml.layout.export.interfaces.NodeGroupMap;

/**
 * 
 * @author Dávid János Németh
 *
 */
@SuppressWarnings("serial")
public class NodeGroupMapImpl extends HashMap<Class<? extends LayoutNodeGroup>, NodeGroupInfo> implements NodeGroupMap {
    
}
