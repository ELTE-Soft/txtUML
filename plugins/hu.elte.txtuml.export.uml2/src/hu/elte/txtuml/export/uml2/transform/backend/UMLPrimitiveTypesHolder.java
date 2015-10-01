package hu.elte.txtuml.export.uml2.transform.backend;

import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Profile;

/**
 * Instances of this class hold UML primitive types.
 * 
 * @author Adam Ancsin
 *
 */
public final class UMLPrimitiveTypesHolder {
	/**
	 * The UML2 Integer primitive type.
	 */
	private PrimitiveType UML2Integer;

	/**
	 * The UML2 Boolean primitive type.
	 */
	private PrimitiveType UML2Boolean;

	/**
	 * The UML2 String primitive type.
	 */
	private PrimitiveType UML2String;

	/**
	 * The UML2 Real primitive type.
	 */
	private PrimitiveType UML2Real;

	/**
	 * The UML2 UnlimitedNatural primitive type.
	 */
	private PrimitiveType UML2UnlimitedNatural;

	/**
	 * Creates an <code>UMLPrimitiveTypesHolder</code> instance based on the
	 * specified profile.
	 * 
	 * @param profile
	 *            The specified profile.
	 *
	 * @return The created <code>UMLPrimitiveTypesHolder</code> instance.
	 * @author Adam Ancsin
	 */
	public static UMLPrimitiveTypesHolder createFromProfile(Profile profile) {
		UMLPrimitiveTypesHolder created = new UMLPrimitiveTypesHolder();
		
		created.UML2Integer = (PrimitiveType) profile
				.getImportedMember("Integer");
		created.UML2Boolean = (PrimitiveType) profile
				.getImportedMember("Boolean");
		created.UML2String = (PrimitiveType) profile
				.getImportedMember("String");
		created.UML2Real = (PrimitiveType) profile.getImportedMember("Real");
		created.UML2UnlimitedNatural = (PrimitiveType) profile
				.getImportedMember("UnlimitedNatural");

		return created;
	}

	/**
	 * Gets the UML2 Integer primitive type.
	 * 
	 * @return The UML2 Integer primitive type.
	 *
	 * @author Adam Ancsin
	 */
	public PrimitiveType getInteger() {
		return this.UML2Integer;
	}

	/**
	 * Gets the UML2 Boolean primitive type.
	 * 
	 * @return The UML2 Boolean primitive type.
	 *
	 * @author Adam Ancsin
	 */
	public PrimitiveType getBoolean() {
		return this.UML2Boolean;
	}

	/**
	 * Gets the UML2 Real primitive type.
	 * 
	 * @return The UML2 Real primitive type.
	 *
	 * @author Adam Ancsin
	 */
	public PrimitiveType getReal() {
		return this.UML2Real;
	}

	/**
	 * Gets the UML2 String primitive type.
	 * 
	 * @return The UML2 String primitive type.
	 *
	 * @author Adam Ancsin
	 */
	public PrimitiveType getString() {
		return this.UML2String;
	}

	/**
	 * Gets the UML2 UnlimitedNatural primitive type.
	 * 
	 * @return The UML2 UnlimitedNatural primitive type.
	 *
	 * @author Adam Ancsin
	 */
	public PrimitiveType getUnlimitedNatural() {
		return this.UML2UnlimitedNatural;
	}
}
