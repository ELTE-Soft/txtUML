package hu.elte.txtuml.export.javascript.json.model.cd;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Classifier;

import hu.elte.txtuml.export.uml2.mapping.ModelMapProvider;
import hu.elte.txtuml.layout.visualizer.model.AssociationType;
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
	@XmlAccessMethods(getMethodName = "getClasses")
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
	public ClassDiagram(String diagramName, Set<RectangleObject> nodes, Set<LineAssociation> links,
			ModelMapProvider map, double spacing) throws UnexpectedEndException {
		this.spacing = spacing;
		name = diagramName;
		classes = new ArrayList<ClassNode>();
		attributeLinks = new ArrayList<ClassAttributeLink>();
		nonAttributeLinks = new ArrayList<ClassLink>();

		// creating ClassNodes
		for (RectangleObject node : nodes) {
			Classifier clazz = (Classifier) map.getByName(node.getName());
			classes.add(new ClassNode(node, clazz));
		}

		// creating and sorting links into attributeLinks and nonAttributeLinks
		for (LineAssociation link : links) {
			if (link.getType() == AssociationType.generalization) {
				nonAttributeLinks.add(new ClassLink(link, AssociationType.generalization.toString()));
			} else {
				Association assoc = (Association) map.getByName(link.getId());
				attributeLinks.add(new ClassAttributeLink(link, assoc, (Classifier) map.getByName(link.getFrom()),
						(Classifier) map.getByName(link.getTo())));
			}
		}

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
