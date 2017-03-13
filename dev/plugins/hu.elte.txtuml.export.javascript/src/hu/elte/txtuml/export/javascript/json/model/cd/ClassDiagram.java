package hu.elte.txtuml.export.javascript.json.model.cd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;
import org.eclipse.swt.widgets.Item;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Classifier;

import hu.elte.txtuml.export.javascript.scalers.ClassScaler;
import hu.elte.txtuml.export.javascript.scalers.NodeScaler;
import hu.elte.txtuml.export.papyrus.elementsarrangers.ArrangeException;
import hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout.LayoutVisualizerManager;
import hu.elte.txtuml.export.uml2.mapping.ModelMapProvider;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.visualizer.model.AssociationType;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.export.diagrams.common.LayoutTransformer;

/**
 * 
 * Holds information about a class diagram and it's elements
 *
 */
public class ClassDiagram {

	@XmlAccessMethods(getMethodName = "getName")
	private String name;
	@XmlTransient
	private Map<String,ClassNode> classes;
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
	 * @param nodes
	 *            The nodes of the already arranged diagram which are containing
	 *            abstract positions and sizes
	 * @param links
	 *            The links of the already arranged diagram which are containing
	 *            abstract routes
	 * @param map
	 *            The map which links the layout informations to the EMF-UML
	 *            model
	 * @param spacing
	 *            The desired spacing
	 * @throws UnexpectedEndException
	 *             Exception is thrown if an association's end could not be
	 *             linked to the EMF-UML model
	 */
	public ClassDiagram(String diagramName, DiagramExportationReport der,
			ModelMapProvider map) throws UnexpectedEndException, ArrangeException {
		name = diagramName;
		classes = new HashMap<String, ClassNode>();
		attributeLinks = new ArrayList<ClassAttributeLink>();
		nonAttributeLinks = new ArrayList<ClassLink>();

		Set<RectangleObject> nodes = der.getNodes();
		Set<LineAssociation> links = der.getLinks();
		// creating ClassNodes
		for (RectangleObject node : nodes) {
			Classifier clazz = (Classifier) map.getByName(node.getName());
			ClassNode cn = new ClassNode(clazz);
			NodeScaler scaler = new ClassScaler(cn);
			node.setPixelWidth(scaler.getWidth());
			node.setPixelHeight(scaler.getHeight());
			classes.put(node.getName(), cn);
		}
		
		LayoutVisualizerManager lvm = new LayoutVisualizerManager(nodes, der.getLinks(), der.getStatements());
		lvm.arrange();
		LayoutTransformer lt = new LayoutTransformer(lvm.getPixelGridRatioHorizontal(), lvm.getPixelGridRatioVertical());
		
		//Map<String, Rectangle> ltrmap =  nodes.stream().collect(Collectors.toMap(RectangleObject::getName, Function.identity()));
		//Map<String, List<Point>> ltpmap =  links.stream().collect(Collectors.toMap(LineAssociation::getId, la -> la.getRoute()));
		
		
		//lt.doTranformations(ltrmap, ltpmap);
		
		
		
		
		for (RectangleObject node : nodes) {
			classes.get(node.getName()).setLayout(node);
		}
		
		
		// creating and sorting links into attributeLinks and nonAttributeLinks
		for (LineAssociation link : der.getLinks()) {
			if (link.getType() == AssociationType.generalization) {
				nonAttributeLinks.add(new ClassLink(link));
			} else {
				Association assoc = (Association) map.getByName(link.getId());
				attributeLinks.add(new ClassAttributeLink(link, assoc, (Classifier) map.getByName(link.getFrom()),
						(Classifier) map.getByName(link.getTo())));
			}
		}
		System.out.println(lvm.getPixelGridRatioHorizontal());

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
	@XmlElement(name="classes")
	public Collection<ClassNode> getClasses() {
		return classes.values();
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
