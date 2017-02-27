package hu.elte.txtuml.export.javascript.json.model.smd;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;
import org.eclipse.uml2.uml.PseudostateKind;

import hu.elte.txtuml.export.javascript.json.EnumAdapter;
import hu.elte.txtuml.layout.visualizer.model.Point;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

/**
 * 
 * Holds information about a pseudo state of a statemachine
 *
 */
public class PseudoState {
	@XmlAccessMethods(getMethodName = "getId")
	private String id;
	@XmlAccessMethods(getMethodName = "getName")
	private String name;
	@XmlAccessMethods(getMethodName = "getKind")
	@XmlJavaTypeAdapter(EnumAdapter.class)
	private PseudostateKind kind;
	@XmlAccessMethods(getMethodName = "getPosition")
	private Point position;
	@XmlAccessMethods(getMethodName = "getWidth")
	private Integer width;
	@XmlAccessMethods(getMethodName = "getHeight")
	private Integer height;

	/**
	 * No-arg constructor required for serialization
	 */
	protected PseudoState() {
	}

	/**
	 * Creates a PseudoState based on the EMF-UML model-element and layout
	 * information provided
	 * 
	 * @param node
	 *            the layout information
	 * @param state
	 *            the EMF-UML model-element which holds informations of this
	 *            diagram element
	 */
	public PseudoState(RectangleObject node, org.eclipse.uml2.uml.Pseudostate state) {
		id = node.getName();
		name = state.getLabel();
		kind = state.getKind();
		position = node.getPosition();
		width = node.getWidth();
		height = node.getHeight();

	}

	/**
	 * 
	 * @return the layout descriptor ID of the pseudo state
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @return the name of the pseudo state
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return the kind of the pseudo state
	 */
	public PseudostateKind getKind() {
		return kind;
	}

	/**
	 * 
	 * @return the position of the pseudo state
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * 
	 * @return the width of the pseudo state
	 */
	public Integer getWidth() {
		return width;
	}

	/**
	 * 
	 * @return the height of the pseudo state
	 */
	public Integer getHeight() {
		return height;
	}

}
