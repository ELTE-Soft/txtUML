package hu.elte.txtuml.export.javascript.json.model.smd;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;

import hu.elte.txtuml.export.javascript.json.MarshalablePoint;
import hu.elte.txtuml.utils.diagrams.Rectangle;

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
	private MarshalablePoint position;
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
	 * @param state
	 *            the EMF-UML model-element which holds informations of this
	 *            diagram element
	 * 
	 * @param id
	 *            the layout id of this element
	 */
	public State(org.eclipse.uml2.uml.State state, String id) {
		this.id = id;
		name = state.getLabel();
	}

	/**
	 * Sets the bounding box of this element
	 * 
	 * @param rectangle
	 *            the bounding box to be set
	 */
	public void setLayout(Rectangle rectangle) {
		width = rectangle.width();
		height = rectangle.height();
		position = new MarshalablePoint(rectangle.getTopLeft());
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
	public MarshalablePoint getPosition() {
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
