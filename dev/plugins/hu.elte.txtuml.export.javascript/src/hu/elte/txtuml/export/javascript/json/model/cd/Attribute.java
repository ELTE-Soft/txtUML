package hu.elte.txtuml.export.javascript.json.model.cd;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;
import org.eclipse.uml2.uml.Property;

/**
 * 
 * Holds information about an argument of a class
 *
 */
public class Attribute extends ClassMember {
	@XmlAccessMethods(getMethodName = "getType")
	private String type;

	/**
	 * No-arg constructor required for serialization
	 */
	protected Attribute() {
	}

	/**
	 * Creates an Attribute based on the EMF-UML model-element provided
	 * 
	 * @param attr
	 *            the EMF-UML model-element which holds informations of this
	 *            diagram element
	 */
	public Attribute(Property attr) {
		super(attr.getName(), attr.getVisibility());
		type = attr.getType().getName();
	}

	/**
	 * 
	 * @return the type of the attribute
	 */
	public String getType() {
		return type;
	}

}
