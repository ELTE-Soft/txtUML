package hu.elte.txtuml.layout.export.interfaces;

import hu.elte.txtuml.layout.export.DiagramType;
import hu.elte.txtuml.layout.export.elementinfo.ElementInfo;
import hu.elte.txtuml.layout.export.impl.ElementExporterImpl;
import hu.elte.txtuml.layout.lang.elements.LayoutElement;
import hu.elte.txtuml.layout.lang.elements.LayoutNode;
import hu.elte.txtuml.layout.lang.elements.LayoutNonGroupElement;

/**
 * 
 * @author Gábor Ferenc Kovács
 *
 */
public interface ElementExporter {

	static ElementExporter create(NodeMap nodes, LinkMap links) {
		return new ElementExporterImpl(nodes, links);
	}

	DiagramType getDiagramTypeBasedOnElements();
	
	/**
	 * Get info about the given element and register it if its of a valid type.
	 */
	ElementInfo exportElement(Class<? extends LayoutElement> element);

	/**
	 * Specialization to the <code>exportElement</code> method. Using
	 * <code>exportElement</code> is always sufficient, but this method might be
	 * more efficient.
	 */
	ElementInfo exportNonGroupElement(
			Class<? extends LayoutNonGroupElement> element);

	/**
	 * Specialization to the <code>exportElement</code> method. Using
	 * <code>exportElement</code> is always sufficient, but this method might be
	 * more efficient.
	 */
	ElementInfo exportNode(Class<? extends LayoutNode> val);

}
