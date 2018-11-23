package hu.elte.txtuml.export.javascript.json.model.cd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Classifier;

import hu.elte.txtuml.export.diagrams.common.Point;
import hu.elte.txtuml.export.diagrams.common.Rectangle;
import hu.elte.txtuml.export.diagrams.common.arrange.ArrangeException;
import hu.elte.txtuml.export.diagrams.common.arrange.LayoutTransformer;
import hu.elte.txtuml.export.diagrams.common.arrange.LayoutVisualizerManager;
import hu.elte.txtuml.export.javascript.scalers.ClassScaler;
import hu.elte.txtuml.export.javascript.scalers.NodeScaler;
import hu.elte.txtuml.export.javascript.utils.LinkUtils;
import hu.elte.txtuml.export.javascript.utils.NodeUtils;
import hu.elte.txtuml.export.uml2.mapping.ModelMapProvider;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.visualizer.model.AssociationType;
import hu.elte.txtuml.layout.visualizer.model.DiagramType;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

/**
 * 
 * Holds information about a class diagram and it's elements
 *
 */
public class ClassDiagram {

	@XmlAccessMethods(getMethodName = "getName")
	private String name;
	@XmlElement(name = "classes")
	private List<ClassNode> classes;
	@XmlAccessMethods(getMethodName = "getAttributeLinks")
	private List<ClassAttributeLink> attributeLinks;
	@XmlAccessMethods(getMethodName = "getNonAttributeLinks")
	private List<ClassLink> nonAttributeLinks;
	@XmlAccessMethods(getMethodName = "getSpacing")
	private double spacing;

	/**
	 * No-arg constructor required for serialization
	 */
	protected ClassDiagram() {
	}

	/**
	 * Creates a ClassDiagram based on the EMF-UML model's information and
	 * layout information provided
	 * 
	 * @param diagramName
	 *            The name of the diagram
	 * @param der
	 *            The layout of the diagram
	 * @param map
	 *            The map which links the layout informations to the EMF-UML
	 *            model
	 * @param spacing
	 *            The desired spacing
	 * @throws UnexpectedEndException
	 *             Exception is thrown if an association's end could not be
	 *             linked to the EMF-UML model
	 * @throws ArrangeException
	 *             Exception is thrown if a diagram could not be arranged
	 * @throws UnexpectedException
	 *             Exception is thrown if a diagram contains unexpected parts
	 */
	public ClassDiagram(String diagramName, DiagramExportationReport der, ModelMapProvider map,
			ClassDiagramPixelDimensionProvider provider, IProgressMonitor monitor) throws UnexpectedEndException, ArrangeException {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		name = diagramName;
		classes = new ArrayList<ClassNode>();
		attributeLinks = new ArrayList<ClassAttributeLink>();
		nonAttributeLinks = new ArrayList<ClassLink>();

		Set<RectangleObject> nodes = der.getNodes();
		Set<LineAssociation> links = der.getLinks();
		
		// creating ClassNodes
		for (RectangleObject node : nodes) {
			if(subMonitor.isCanceled()) return;
			Classifier clazz = (Classifier) map.getByName(node.getName());
			ClassNode cn = new ClassNode(clazz, node.getName());

			// setting the correct size for ClassNodes
			NodeScaler scaler = new ClassScaler(cn);
			node.setPixelWidth(scaler.getWidth());
			node.setPixelHeight(scaler.getHeight());

			classes.add(cn);
		}
		subMonitor.worked(10);
		if(subMonitor.isCanceled()) return;
		// arranging the diagram
		LayoutVisualizerManager lvm = new LayoutVisualizerManager(nodes, links, der.getStatements(), DiagramType.Class,
				provider);
		lvm.addProgressMonitor(subMonitor.newChild(80));
		lvm.arrange();

		subMonitor.subTask("Applying transformations...");
		if(subMonitor.isCanceled()) return;
		// scaling and transforming nodes and links
		LayoutTransformer lt = new LayoutTransformer();
		Map<String, Rectangle> ltrmap = NodeUtils.getRectMapfromROCollection(lvm.getObjects());
		Map<String, List<Point>> ltpmap = LinkUtils.getPointMapfromLACollection(lvm.getAssociations());

		lt.doTranformations(ltrmap, ltpmap);

		// setting the final layout to ClassNodes
		for (ClassNode cn : classes) {
			cn.setLayout(ltrmap.get(cn.getId()));
		}
		subMonitor.worked(5);

		subMonitor.subTask("Creating associations...");
		// creating and sorting links into attributeLinks and nonAttributeLinks
		for (LineAssociation link : der.getLinks()) {
			if(subMonitor.isCanceled()) return;
			ClassLink cl;
			if (link.getType() == AssociationType.generalization) {
				cl = new ClassLink(link);
				nonAttributeLinks.add(cl);
			} else {
				Association assoc = (Association) map.getByName(link.getId());
				cl = new ClassAttributeLink(link, assoc, (Classifier) map.getByName(link.getFrom()),
						(Classifier) map.getByName(link.getTo()));
				attributeLinks.add((ClassAttributeLink) cl);
			}
			// setting the final route of the links
			cl.setRoute(ltpmap.get(link.getId()));
		}
		subMonitor.done();
	}

	/**
	 * 
	 * @return the name of the diagram
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return the ClassNodes of the diagram
	 */

	public List<ClassNode> getClasses() {
		return classes;
	}

	/**
	 * 
	 * @return the attribute links of the diagram
	 */
	public List<ClassAttributeLink> getAttributeLinks() {
		return attributeLinks;
	}

	/**
	 * 
	 * @return the generic links of the diagram
	 */
	public List<ClassLink> getNonAttributeLinks() {
		return nonAttributeLinks;
	}

	/**
	 * 
	 * @return the spacing of the diagram
	 */
	public double getSpacing() {
		return spacing;
	}

}
