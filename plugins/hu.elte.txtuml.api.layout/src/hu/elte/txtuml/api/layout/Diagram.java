package hu.elte.txtuml.api.layout;

import hu.elte.txtuml.api.layout.elements.LayoutLinkGroup;
import hu.elte.txtuml.api.layout.elements.LayoutNodeGroup;
import hu.elte.txtuml.api.layout.elements.LayoutPhantomNode;

public abstract class Diagram {

	public abstract class Phantom implements LayoutPhantomNode {
	}

	public abstract class NodeGroup implements LayoutNodeGroup {
	}

	public abstract class LinkGroup implements LayoutLinkGroup {
	}

	public abstract class Layout {
	}

}
