package hu.elte.txtuml.layout.export.elementinfo.impl;

import hu.elte.txtuml.api.layout.elements.LayoutElement;
import hu.elte.txtuml.api.layout.elements.LayoutLink;
import hu.elte.txtuml.layout.export.elementinfo.impl.ElementInfoImpl;
import hu.elte.txtuml.layout.export.elementinfo.ElementType;
import hu.elte.txtuml.layout.export.elementinfo.LinkGroupInfo;
import hu.elte.txtuml.layout.export.elementinfo.LinkInfo;
import hu.elte.txtuml.layout.export.interfaces.LinkMap;
import hu.elte.txtuml.layout.export.DiagramType;

/**
 * 
 * @author Dávid János Németh
 *
 */
public class LinkGroupInfoImpl extends ElementInfoImpl implements LinkGroupInfo {
    
    private final LinkMap links;
    private boolean beingExported;
    
    public LinkGroupInfoImpl(Class<? extends LayoutElement> elementClass, DiagramType diagType, String asString) {
        super(elementClass, diagType, asString);
        links = LinkMap.create();
        beingExported = false;
    }
    
    @Override
    public ElementType getType() {
        return ElementType.LinkGroup;
    }

    @Override
    public LinkGroupInfo asLinkGroupInfo() {
        return this;
    }
    
    @Override
    public boolean beingExported() {
        return beingExported;
    }
    
    @Override
    public void setBeingExported(boolean val) {
        beingExported = val;
    }
    
    @Override
    public LinkMap getAllLinks() {
        return links;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void addLink(LinkInfo link) {
        links.put((Class<? extends LayoutLink>) link.getElementClass(), link);
    }
    
}
