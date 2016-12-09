package hu.elte.txtuml.export.javascript.json.model.smd;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;

import hu.elte.txtuml.layout.visualizer.model.Point;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

/**
 * 
 * Holds information about a state of a statemachine
 *
 */
public class State {
	@XmlAccessMethods(getMethodName = "getId")
	private String id;
	@XmlAccessMethods(getMethodName = "getName")
	private String name;
	@XmlAccessMethods(getMethodName = "getPosition")
	private Point position;
	@XmlAccessMethods(getMethodName = "getWidth")
	private Integer width;
	@XmlAccessMethods(getMethodName = "getHeight")
	private Integer height;

	/**
	 * No-arg constructor required for serialization
	 */
	protected State() {
	}

	/**
	 * Creates a State based on the EMF-UML model-element and layout information
	 * provided
	 * 
	 * @param node
	 *            the layout information
	 * @param state
	 *            the EMF-UML model-element which holds informations of this
	 *            diagram element
	 */
	public State(RectangleObject node, org.eclipse.uml2.uml.State state) {
		id = node.getName();
		name = state.getLabel();
		position = node.getPosition();
		width = node.getWidth();
		height = node.getHeight();
	}

	/**
	 * 
	 * @return the layout descriptor ID of the state
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @return the name of the state
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return the position of the state
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * 
	 * @return the width of the state
	 */
	public Integer getWidth() {
		return width;
	}

	/**
	 * 
	 * @return the height of the state
	 */
	public Integer getHeight() {
		return height;
	}

}
