package hu.elte.txtuml.export.javascript.json.model.cd;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;
import org.eclipse.uml2.uml.VisibilityKind;

import hu.elte.txtuml.export.javascript.json.EnumAdapter;

/**
 * 
 * Holds information about a member of a class
 *
 */
public abstract class ClassMember {
	@XmlAccessMethods(getMethodName = "getVisibility")
	@XmlJavaTypeAdapter(EnumAdapter.class)
	protected VisibilityKind visibility;
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
		this.visibility = visibility;
	}

	/**
	 * 
	 * @return the visibility of the class member
	 */
	public VisibilityKind getVisibility() {
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
