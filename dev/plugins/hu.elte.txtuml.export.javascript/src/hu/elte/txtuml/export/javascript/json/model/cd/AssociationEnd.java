package hu.elte.txtuml.export.javascript.json.model.cd;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;
import org.eclipse.uml2.uml.Property;

/**
 * 
 * Holds information about an end of an association
 *
 */
public class AssociationEnd {
	@XmlAccessMethods(getMethodName = "getName")
	private String name;
	@XmlAccessMethods(getMethodName = "getMultiplicity")
	private String multiplicity;
	@XmlAccessMethods(getMethodName = "getVisibility")
	private String visibility;
	@XmlAccessMethods(getMethodName = "isNavigable")
	private Boolean navigable;
	@XmlAccessMethods(getMethodName = "isComposition")
	private Boolean composition;

	/**
	 * No-arg constructor required for serialization
	 */
	protected AssociationEnd() {
	};

	/**
	 * Creates an AssociationEnd based on the EMF-UML model-element provided
	 * 
	 * @param end
	 *            the EMF-UML model-element which holds informations of this
	 *            diagram element
	 */
	public AssociationEnd(Property end) {
		name = end.getLabel();
		int low = end.lowerBound();
		int high = end.upperBound();

		// Create an UML-style multiplicity string
		if (low == 0 && high == -1) {
			multiplicity = "*";
		} else if (low == high) {
			multiplicity = Integer.toString(low);
		} else {
			multiplicity = low + ".." + (high == -1 ? "*" : high);
		}
		visibility = end.getVisibility().getLiteral();
		navigable = end.isNavigable();
		composition = end.isComposite();
	}

	/**
	 * 
	 * @return the name of the end
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return the multiplicity of the end
	 */
	public String getMultiplicity() {
		return multiplicity;
	}

	/**
	 * 
	 * @return the visibility of the end
	 */
	public String getVisibility() {
		return visibility;
	}

	/**
	 * 
	 * @return whether the end is navigable
	 */
	public Boolean isNavigable() {
		return navigable;
	}

	/**
	 * 
	 * @return whether the end is a composition
	 */
	public Boolean isComposition() {
		return composition;
	}

}
