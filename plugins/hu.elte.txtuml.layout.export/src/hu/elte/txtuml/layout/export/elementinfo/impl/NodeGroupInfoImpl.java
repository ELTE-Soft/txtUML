package hu.elte.txtuml.layout.export.elementinfo.impl;

import hu.elte.txtuml.layout.export.elementinfo.impl.ElementInfoImpl;
import hu.elte.txtuml.layout.export.elementinfo.ElementType;
import hu.elte.txtuml.layout.export.elementinfo.NodeGroupInfo;
import hu.elte.txtuml.layout.export.elementinfo.NodeInfo;
import hu.elte.txtuml.layout.export.interfaces.NodeMap;
import hu.elte.txtuml.layout.lang.AlignmentType;
import hu.elte.txtuml.layout.lang.elements.LayoutElement;
import hu.elte.txtuml.layout.lang.elements.LayoutNode;
import hu.elte.txtuml.layout.export.DiagramType;

/**
 * 
 * @author Dávid János Németh
 *
 */
public class NodeGroupInfoImpl extends ElementInfoImpl implements NodeGroupInfo {
    
    private final NodeMap nodes;
    private AlignmentType alignment;
    private boolean beingExported;
    
    public NodeGroupInfoImpl(Class<? extends LayoutElement> elementClass, DiagramType diagType, String asString) {
        super(elementClass, diagType, asString);
        nodes = NodeMap.create();
        alignment = null;
        beingExported = false;
    }
    
    @Override
    public ElementType getType() {
        return ElementType.NodeGroup;
    }

    @Override
    public NodeGroupInfo asNodeGroupInfo() {
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
    public NodeMap getAllNodes() {
        return nodes;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void addNode(NodeInfo link) {
        nodes.put((Class<? extends LayoutNode>) link.getElementClass(), link);
    }

    @Override
    public void setAlignment(AlignmentType alignment) {
        this.alignment = alignment;
    }

    @Override
    public AlignmentType getAlignment() {
        return alignment;
    }
    
}
