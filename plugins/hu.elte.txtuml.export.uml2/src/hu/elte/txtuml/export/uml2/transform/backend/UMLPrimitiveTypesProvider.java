package hu.elte.txtuml.export.uml2.transform.backend;

import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Profile;

/**
 * This class is responsible for providing the UML primitive types.
 * @author Adam Ancsin
 *
 */
public final class UMLPrimitiveTypesProvider 
{
	/**
	 * The UML2 Integer primitive type.
	 */
	private static PrimitiveType UML2Integer;
	
	/**
	 * The UML2 Boolean primitive type.
	 */
	private static PrimitiveType UML2Boolean;
	
	/**
	 * The UML2 String primitive type.
	 */
	private static PrimitiveType UML2String;
	
	/**
	 * The UML2 Real primitive type.
	 */
	private static PrimitiveType UML2Real;
	
	/**
	 * The UML2 UnlimitedNatural primitive type.
	 */
	private static PrimitiveType UML2UnlimitedNatural;
	
	/**
	 * Imports the UML primitive type from the specified profile.
	 * @param profile The specified profile.
	 *
	 * @author Adam Ancsin
	 */
	public static void importFromProfile(Profile profile)
	{
		UML2Integer=(PrimitiveType) profile.getImportedMember("Integer");
		UML2Boolean=(PrimitiveType) profile.getImportedMember("Boolean");
		UML2String=(PrimitiveType) profile.getImportedMember("String");
		UML2Real=(PrimitiveType) profile.getImportedMember("Real");
		UML2UnlimitedNatural=(PrimitiveType) profile.getImportedMember("UnlimitedNatural");	
	}
	
	/**
	 * Gets the UML2 Integer primitive type.
	 * @return The UML2 Integer primitive type.
	 *
	 * @author Adam Ancsin
	 */
	public static PrimitiveType getInteger()
	{
		return UML2Integer;
	}
	
	/**
	 * Gets the UML2 Boolean primitive type.
	 * @return The UML2 Boolean primitive type.
	 *
	 * @author Adam Ancsin
	 */
	public static PrimitiveType getBoolean()
	{
		return UML2Boolean;
	}
	
	/**
	 * Gets the UML2 Real primitive type.
	 * @return The UML2 Real primitive type.
	 *
	 * @author Adam Ancsin
	 */
	public static PrimitiveType getReal()
	{
		return UML2Real;
	}
	
	/**
	 * Gets the UML2 String primitive type.
	 * @return The UML2 String primitive type.
	 *
	 * @author Adam Ancsin
	 */
	public static PrimitiveType getString()
	{
		return UML2String;
	}
	
	/**
	 * Gets the UML2 UnlimitedNatural primitive type.
	 * @return The UML2 UnlimitedNatural primitive type.
	 *
	 * @author Adam Ancsin
	 */
	public static PrimitiveType getUnlimitedNatural()
	{
		return UML2UnlimitedNatural;
	}
}
