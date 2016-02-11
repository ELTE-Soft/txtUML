package hu.elte.txtuml.layout.export.elementinfo.impl;

import hu.elte.txtuml.api.layout.AlignmentType;
import hu.elte.txtuml.layout.export.elementinfo.NodeGroupInfo;
import hu.elte.txtuml.layout.export.elementinfo.NodeInfo;
import hu.elte.txtuml.layout.export.interfaces.NodeMap;

public class NodeGroupInfoImpl extends ElementInfoImpl implements NodeGroupInfo {

	private final NodeMap nodes;
	private AlignmentType alignment;
	private boolean beingExported;

	public NodeGroupInfoImpl(Class<?> elementClass, String asString) {
		super(elementClass, asString);
		nodes = NodeMap.create();
		alignment = null;
		beingExported = false;
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
	public void addNode(NodeInfo node) {
		nodes.put(node.getElementClass(), node);
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
