package hu.elte.txtuml.layout.export.interfaces;

import java.util.Set;

import hu.elte.txtuml.layout.export.DiagramType;
import hu.elte.txtuml.layout.export.elementinfo.ElementInfo;
import hu.elte.txtuml.layout.export.elementinfo.NodeInfo;
import hu.elte.txtuml.layout.export.impl.ElementExporterImpl;
import hu.elte.txtuml.layout.export.problems.ProblemReporter;
import hu.elte.txtuml.layout.lang.elements.LayoutAbstractNode;
import hu.elte.txtuml.layout.lang.elements.LayoutElement;
import hu.elte.txtuml.layout.lang.elements.LayoutGroup;
import hu.elte.txtuml.layout.lang.elements.LayoutLink;
import hu.elte.txtuml.layout.lang.elements.LayoutLinkGroup;
import hu.elte.txtuml.layout.lang.elements.LayoutNode;
import hu.elte.txtuml.layout.lang.elements.LayoutNodeGroup;
import hu.elte.txtuml.layout.lang.elements.LayoutNonGroupElement;
import hu.elte.txtuml.layout.lang.elements.LayoutPhantomNode;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

/**
 * 
 * @author Gábor Ferenc Kovács
 *
 */
public interface ElementExporter {

    static ElementExporter create(ProblemReporter problemReporter) {
        return new ElementExporterImpl(problemReporter);
    }

	DiagramType getDiagramTypeBasedOnElements();
	
	String getRootElementAsString();
	
	NodeMap getNodes();
	
	NodeList getPhantoms();
	
	LinkMap getLinks();
	
	Set<RectangleObject> getNodesAsObjects();
	
	Set<LineAssociation> getLinksAsLines();
	
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
	
	ElementInfo exportGroupElement(Class<? extends LayoutGroup> element);

	/**
	 * Specialization to the <code>exportElement</code> method. Using
	 * <code>exportElement</code> is always sufficient, but this method might be
	 * more efficient.
	 */
	ElementInfo exportNode(Class<? extends LayoutNode> node);
	
	ElementInfo exportPhantom(Class<? extends LayoutPhantomNode> phantom);
	
	ElementInfo exportLink(Class<? extends LayoutLink> link);
    
	/**
     * Should be called only by an instance of class {@link hu.elte.txtuml.layout.export.diagramexporters.ClassDiagramExporter DiagramExporter}.
     * Checking the diagram type is not necessary since it's checked by the caller.
     */
	void exportGeneralization(Class<? extends LayoutNode> base, Class<? extends LayoutNode> derived);
	
	ElementInfo exportNodeGroup(Class<? extends LayoutNodeGroup> nodeGroup);
	
	ElementInfo exportLinkGroup(Class<? extends LayoutLinkGroup> linkGroup);
		
	ElementInfo exportAnonNodeGroup(Class<? extends LayoutAbstractNode>[] abstractNodes);
	
	NodeInfo createPhantom();
	
	// exportation finalizer
	
	void exportImpliedLinks();

}
