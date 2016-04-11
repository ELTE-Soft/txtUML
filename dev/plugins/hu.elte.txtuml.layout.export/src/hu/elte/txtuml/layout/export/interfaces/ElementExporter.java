package hu.elte.txtuml.layout.export.interfaces;

import java.util.Set;

import hu.elte.txtuml.api.layout.Diagram.Box;
import hu.elte.txtuml.api.layout.Diagram.LinkGroup;
import hu.elte.txtuml.api.layout.Diagram.NodeGroup;
import hu.elte.txtuml.api.layout.Inside;
import hu.elte.txtuml.layout.export.DiagramType;
import hu.elte.txtuml.layout.export.elementinfo.ConcreteElementInfo;
import hu.elte.txtuml.layout.export.elementinfo.ElementInfo;
import hu.elte.txtuml.layout.export.elementinfo.GroupInfo;
import hu.elte.txtuml.layout.export.elementinfo.LinkGroupInfo;
import hu.elte.txtuml.layout.export.elementinfo.LinkInfo;
import hu.elte.txtuml.layout.export.elementinfo.NodeGroupInfo;
import hu.elte.txtuml.layout.export.elementinfo.NodeInfo;
import hu.elte.txtuml.layout.export.impl.ElementExporterImpl;
import hu.elte.txtuml.layout.export.problems.ElementExportationException;
import hu.elte.txtuml.layout.export.problems.ProblemReporter;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

public interface ElementExporter {

	static ElementExporter create(String sourceProjectName, ProblemReporter problemReporter) {
		return new ElementExporterImpl(sourceProjectName, problemReporter);
	}

	DiagramType getDiagramTypeBasedOnElements();

	String getSourceProjectName();
	
	String getModelName();

	NodeMap getNodes();

	NodeList getPhantoms();

	LinkMap getLinks();

	Set<RectangleObject> getNodesAsObjects();

	Set<LineAssociation> getLinksAsLines();

	/**
	 * Get info about the given element and register it if its of a valid type.
	 * 
	 * @throws ElementExportationException
	 */
	ElementInfo exportElement(Class<?> element)
			throws ElementExportationException;

	/**
	 * Specialization to the <code>exportElement</code> method. Using
	 * <code>exportElement</code> is always sufficient, but this method might be
	 * more efficient.
	 * 
	 * @throws ElementExportationException
	 */
	ConcreteElementInfo exportConcreteElement(Class<?> element)
			throws ElementExportationException;

	/**
	 * Specialization to the <code>exportElement</code> method. Using
	 * <code>exportElement</code> is always sufficient, but this method might be
	 * more efficient.
	 * 
	 * @throws ElementExportationException
	 */
	GroupInfo exportGroupElement(Class<?> element)
			throws ElementExportationException;

	/**
	 * Specialization to the <code>exportElement</code> method. Using
	 * <code>exportElement</code> is always sufficient, but this method might be
	 * more efficient.
	 * 
	 * @throws ElementExportationException
	 */
	NodeInfo exportNode(Class<?> node) throws ElementExportationException;

	/**
	 * Specialization to the <code>exportElement</code> method. Using
	 * <code>exportElement</code> is always sufficient, but this method might be
	 * more efficient.
	 * 
	 * @throws ElementExportationException
	 */
	NodeInfo exportPhantom(Class<?> phantom)
			throws ElementExportationException;
	
	void startOfParent(Class<?> parent);
	void setParent(Class<?> child, Class<?> parent);
	void endOfParent();
	
	/**
	 * Specialization to the <code>exportElement</code> method. Using
	 * <code>exportElement</code> is always sufficient, but this method might be
	 * more efficient.
	 * 
	 * @throws ElementExportationException
	 */
	LinkInfo exportLink(Class<?> link) throws ElementExportationException;

	/**
	 * Should be called only by an instance of class
	 * {@link hu.elte.txtuml.layout.export.source.ClassDiagramExporter
	 * ClassDiagramExporter}. Checking the diagram type is not necessary since
	 * it's checked by the caller.
	 * @throws ElementExportationException 
	 */
	void exportGeneralization(Class<?> base, Class<?> derived) throws ElementExportationException;

	/**
	 * Specialization to the <code>exportElement</code> method. Using
	 * <code>exportElement</code> is always sufficient, but this method might be
	 * more efficient.
	 * 
	 * @throws ElementExportationException
	 */
	NodeGroupInfo exportNodeGroup(Class<?> nodeGroup)
			throws ElementExportationException;

	/**
	 * Specialization to the <code>exportElement</code> method. Using
	 * <code>exportElement</code> is always sufficient, but this method might be
	 * more efficient.
	 * 
	 * @throws ElementExportationException
	 */
	LinkGroupInfo exportLinkGroup(Class<?> linkGroup)
			throws ElementExportationException;

	/**
	 * Specialization to the <code>exportElement</code> method. Using
	 * <code>exportElement</code> is always sufficient, but this method might be
	 * more efficient.
	 * 
	 * @throws ElementExportationException
	 */
	NodeGroupInfo exportAnonymousNodeGroup(Class<?>[] abstractNodes)
			throws ElementExportationException;

	NodeInfo createPhantom();

	static boolean isBox(Class<?> cls) {
		return Box.class.isAssignableFrom(cls);
	}
	
	static boolean isPhantom(Class<?> cls) {
		boolean isPresent = cls.isAnnotationPresent(Inside.class);
		
		return isBox(cls) && !isPresent;
	}
	
	static boolean isBoxContainer(Class<?> cls) {
		boolean isPresent = cls.isAnnotationPresent(Inside.class);
		
		return isBox(cls) && isPresent;
	}

	static boolean isNodeGroup(Class<?> cls) {
		return NodeGroup.class.isAssignableFrom(cls);
	}

	static boolean isLinkGroup(Class<?> cls) {
		return LinkGroup.class.isAssignableFrom(cls);
	}

	// exportation finalizer

	void exportDefaultParentage();
	
	void exportImpliedLinks();

}
