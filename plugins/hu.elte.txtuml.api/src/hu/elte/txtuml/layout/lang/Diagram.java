package hu.elte.txtuml.layout.lang;

import hu.elte.txtuml.layout.lang.elements.LayoutLinkGroup;
import hu.elte.txtuml.layout.lang.elements.LayoutNodeGroup;
import hu.elte.txtuml.layout.lang.elements.LayoutPhantomNode;

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
