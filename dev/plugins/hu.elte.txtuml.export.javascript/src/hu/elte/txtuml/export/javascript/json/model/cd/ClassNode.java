package hu.elte.txtuml.export.javascript.json.model.cd;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;

import hu.elte.txtuml.export.javascript.json.EnumAdapter;
import hu.elte.txtuml.layout.visualizer.model.Point;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

/**
 * 
 * Holds information about a class of a class diagram
 *
 */
public class ClassNode {

	@XmlAccessMethods(getMethodName = "getPosition")
	private Point position;
	@XmlAccessMethods(getMethodName = "getWidth")
	private Integer width;
	@XmlAccessMethods(getMethodName = "getHeight")
	private Integer height;
	@XmlAccessMethods(getMethodName = "getId")
	private String id;
	@XmlAccessMethods(getMethodName = "getName")
	private String name;
	@XmlAccessMethods(getMethodName = "getType") 
	@XmlJavaTypeAdapter(EnumAdapter.class)
	private CDNodeType type;
	@XmlAccessMethods(getMethodName = "getAttributes")
	private List<Attribute> attributes;
	@XmlAccessMethods(getMethodName = "getOperations")
	private List<MemberOperation> operations;

	/**
	 * No-arg constructor required for serialization
	 */
	protected ClassNode() {
	}

	/**
	 * Creates a ClassNode based on the EMF-UML model-element and layout
	 * information provided
	 * 
	 * @param layout
	 *            the layout information
	 * @param clazz
	 *            the EMF-UML model-element which holds informations of this
	 *            diagram element
	 */
	public ClassNode(RectangleObject layout, Classifier clazz) {
		position = layout.getPosition();
		width = layout.getWidth();
		height = layout.getHeight();
		id = layout.getName();
		name = clazz.getName();
		attributes = new ArrayList<Attribute>();
		// creating attributes
		for (Property attr : clazz.getAttributes()) {
			if (attr.getAssociation() == null) {
				attributes.add(new Attribute(attr));
			}
		}

		operations = new ArrayList<MemberOperation>();
		// creating operations
		for (Operation op : clazz.getOperations()) {
			operations.add(new MemberOperation(op));
		}

		if (clazz.isAbstract()) {
			type = CDNodeType.ABSTRACT_CLASS;
		} else {
			type = CDNodeType.CLASS;
		}
	}

	/**
	 * 
	 * @return the abstract position of the class
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * 
	 * @return the abstract width of the class
	 */
	public Integer getWidth() {
		return width;
	}

	/**
	 * 
	 * @return the abstract height of the class
	 */
	public Integer getHeight() {
		return height;
	}

	/**
	 * 
	 * @return the layout descriptor id of the class
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @return the name of the class
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return the attributes of the class
	 */
	public List<Attribute> getAttributes() {
		return attributes;
	}

	/**
	 * 
	 * @return the operations of the class
	 */

	public List<MemberOperation> getOperations() {
		return operations;
	}

	/**
	 * 
	 * @return the type of the class (note: this currently can be "class" or
	 *         "abstract")
	 */
	public CDNodeType getType() {
		return type;
	}

}
