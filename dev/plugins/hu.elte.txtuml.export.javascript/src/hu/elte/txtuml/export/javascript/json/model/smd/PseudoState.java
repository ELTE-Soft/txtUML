package hu.elte.txtuml.export.javascript.json.model.smd;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;
import org.eclipse.uml2.uml.PseudostateKind;

import hu.elte.txtuml.export.diagrams.common.Rectangle;
import hu.elte.txtuml.export.javascript.json.EnumAdapter;
import hu.elte.txtuml.export.javascript.json.MarshalablePoint;

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
	private MarshalablePoint position;
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
	 * @param state
	 *            the EMF-UML model-element which holds informations of this
	 *            diagram element
	 * 
	 * @param id
	 *            the layout id of this element
	 */
	public PseudoState(org.eclipse.uml2.uml.Pseudostate state, String id) {
		this.id = id;
		name = state.getLabel();
		kind = state.getKind();

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
	public MarshalablePoint getPosition() {
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
