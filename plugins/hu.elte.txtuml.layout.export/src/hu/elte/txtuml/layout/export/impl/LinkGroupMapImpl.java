package hu.elte.txtuml.layout.export.impl;

import java.util.HashMap;

import hu.elte.txtuml.layout.export.elementinfo.LinkGroupInfo;
import hu.elte.txtuml.layout.export.interfaces.LinkGroupMap;
import hu.elte.txtuml.layout.lang.elements.LayoutLinkGroup;

/**
 * 
 * @author Dávid János Németh
 *
 */
@SuppressWarnings("serial")
public class LinkGroupMapImpl extends HashMap<Class<? extends LayoutLinkGroup>, LinkGroupInfo> implements LinkGroupMap {
    
}
