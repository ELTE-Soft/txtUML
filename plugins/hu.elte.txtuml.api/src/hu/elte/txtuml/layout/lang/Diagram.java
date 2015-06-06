package hu.elte.txtuml.layout.lang;

import hu.elte.txtuml.layout.lang.elements.LayoutLinkGroup;
import hu.elte.txtuml.layout.lang.elements.LayoutNode;
import hu.elte.txtuml.layout.lang.elements.LayoutNodeGroup;

public abstract class Diagram {

	public abstract class Phantom implements LayoutNode {
	}

	public abstract class NodeGroup implements LayoutNodeGroup {
	}

	public abstract class LinkGroup implements LayoutLinkGroup {
	}

	public abstract class Layout {
	}

}
