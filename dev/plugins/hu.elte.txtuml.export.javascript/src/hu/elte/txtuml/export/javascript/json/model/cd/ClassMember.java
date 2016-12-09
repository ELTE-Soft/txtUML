package hu.elte.txtuml.export.javascript.json.model.cd;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;
import org.eclipse.uml2.uml.VisibilityKind;

/**
 * 
 * Holds information about a member of a class
 *
 */
public abstract class ClassMember {
	@XmlAccessMethods(getMethodName = "getVisibility")
	protected String visibility;
	@XmlAccessMethods(getMethodName = "getName")
	protected String name;

	/**
	 * No-arg constructor required for serialization
	 */
	protected ClassMember() {
	}

	/**
	 * Creates a ClassMember based on the name and visiblity provided
	 * 
	 * @param name
	 *            name of the class member
	 * @param visibility
	 *            visibility of the class member
	 */
	protected ClassMember(String name, VisibilityKind visibility) {
		this.name = name;
		this.visibility = visibility.getLiteral();
	}

	/**
	 * 
	 * @return the visibility of the class member
	 */
	public String getVisibility() {
		return visibility;
	}

	/**
	 * 
	 * @return the name of the class member
	 */
	public String getName() {
		return name;
	}

}
