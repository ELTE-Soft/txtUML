package hu.elte.txtuml.layout.export.interfaces;

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

	
	ElementInfo exportElement(Class<? extends LayoutElement> element);
	
	ElementInfo exportNonGroupElement(
			Class<? extends LayoutNonGroupElement> element);

	ElementInfo exportNode(Class<? extends LayoutNode> val);

}
