package hu.elte.txtuml.export.uml2.transform.backend;

import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Profile;

public class UMLPrimitiveTypes {

	private static PrimitiveType UML2Integer;
	private static PrimitiveType UML2Boolean;
	private static PrimitiveType UML2String;
	private static PrimitiveType UML2Real;
	private static PrimitiveType UML2UnlimitedNatural;
	
	public static void importFromProfile(Profile profile)
	{
		UML2Integer=(PrimitiveType) profile.getImportedMember("Integer");
		UML2Boolean=(PrimitiveType) profile.getImportedMember("Boolean");
		UML2String=(PrimitiveType) profile.getImportedMember("String");
		UML2Real=(PrimitiveType) profile.getImportedMember("Real");
		UML2UnlimitedNatural=(PrimitiveType) profile.getImportedMember("UnlimitedNatural");	
	}
	public static PrimitiveType getInteger()
	{
		return UML2Integer;
	}
	
	public static PrimitiveType getBoolean()
	{
		return UML2Boolean;
	}
	
	public static PrimitiveType getReal()
	{
		return UML2Real;
	}
	
	public static PrimitiveType getString()
	{
		return UML2String;
	}
	
	public static PrimitiveType getUnlimitedNatural()
	{
		return UML2UnlimitedNatural;
	}
}
