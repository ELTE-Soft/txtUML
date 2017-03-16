package hu.elte.txtuml.export.javascript.json.model.cd;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;
import org.eclipse.uml2.uml.Parameter;

/**
 * 
 * Holds information about an argument of an operation
 *
 */
public class Argument {
	@XmlAccessMethods(getMethodName = "getName")
	private String name;
	@XmlAccessMethods(getMethodName = "getType")
	private String type;

	/**
	 * No-arg constructor required for serialization
	 */
	protected Argument() {
	};

	/**
	 * Creates an Argument based on the EMF-UML model-element provided
	 * 
	 * @param arg
	 *            the EMF-UML model-element which holds informations of this
	 *            diagram element
	 */
	public Argument(Parameter arg) {
		name = arg.getName();
		type = arg.getType().getName();
	}

	/**
	 * 
	 * @return the name of the argument
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return the type of the argument
	 */
	public String getType() {
		return type;
	}
}
