package hu.elte.txtuml.export.uml2.utils;

import hu.elte.txtuml.export.uml2.transform.backend.ImportException;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;

public final class ProfileCreator {

	public static void createProfileForModel(String path, String modelName, ResourceSet resourceSet) throws ImportException
	{
		Profile profile = createProfile(path, modelName);

		Model umlMetamodel = loadUMLMetamodelAndPrimitiveTypes(profile, resourceSet);
		
		createExternalClassStereotypeForProfile(profile, umlMetamodel);

		defineAndSaveProfile(profile, modelName, resourceSet);
		
	}	
	
 	private static void defineAndSaveProfile(Profile profile,String modelName, ResourceSet resourceSet) throws ImportException
 	{
 		profile.define();
		Resource resource = 
				resourceSet.createResource(
						URI.createFileURI("").
						appendSegment("Profile_"+modelName).
						appendFileExtension(UMLResource.PROFILE_FILE_EXTENSION)
				);
		
		resource.getContents().add(profile);
		try
		{
			resource.save(null);

		} 
		catch (IOException ioe)
		{
			throw new ImportException("I/O error occured during model import. Cannot save UML profile.");
		}
 	}
	private static void createExternalClassStereotypeForProfile(Profile profile, Model umlMetamodel)
	{
		//creating the ExternalClass stereotype and an extension for it
		try
		{
			org.eclipse.uml2.uml.Class classifierMetaclass = 
					(org.eclipse.uml2.uml.Class) umlMetamodel.getOwnedType("Classifier");

			profile.createMetaclassReference(classifierMetaclass);

			Stereotype externalStereotype=profile.createOwnedStereotype("ExternalClass", false);
			externalStereotype.createExtension(classifierMetaclass,false);
		}
		catch(Exception e)
		{

		}
	}
	private static Model loadUMLMetamodelAndPrimitiveTypes(Profile profile,ResourceSet resourceSet)
	{
		//loading the UML metamodel
		Model umlMetamodel = (Model)
				loadResource(
						URI.createURI(UMLResource.UML_METAMODEL_URI),
						resourceSet
				);

		//loading UML Primitive Types Library and importing the primitve types from there
		org.eclipse.uml2.uml.Package umlLibrary= 
				loadResource(
						URI.createURI(UMLResource.UML_PRIMITIVE_TYPES_LIBRARY_URI),
						resourceSet
				);
		profile.createElementImport(umlLibrary.getOwnedType("Integer"));
		profile.createElementImport(umlLibrary.getOwnedType("Real"));
		profile.createElementImport(umlLibrary.getOwnedType("Boolean"));
		profile.createElementImport(umlLibrary.getOwnedType("String"));
		profile.createElementImport(umlLibrary.getOwnedType("UnlimitedNatural"));

		return umlMetamodel;
	}
	private static Profile createProfile(String path, String modelName)
	{
		Profile profile = UMLFactory.eINSTANCE.createProfile();
		profile.setName("Custom Profile");
		profile.setURI(
				URI.createFileURI(path).
				appendSegment("Profile_"+modelName).
				appendFileExtension(UMLResource.PROFILE_FILE_EXTENSION).
				toString()
		);
		
		return profile;
	}
	private static org.eclipse.uml2.uml.Package loadResource(URI uri, ResourceSet resourceSet)
	{
		Resource resource=resourceSet.getResource(uri,true);
		return (org.eclipse.uml2.uml.Package) EcoreUtil.getObjectByType(resource.getContents(),UMLPackage.Literals.PACKAGE);
	}
	
}
