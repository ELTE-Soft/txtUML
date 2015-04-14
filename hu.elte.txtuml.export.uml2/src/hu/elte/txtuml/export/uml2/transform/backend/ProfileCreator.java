package hu.elte.txtuml.export.uml2.transform.backend;

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

/**
 * This class provides utilities for creating an UML profile for a model.
 * @author Ádám Ancsin
 *
 */
public final class ProfileCreator {

	/**
	 * Creates the profile for a model represented by the specified class with
	 * the given qualified name in a given output directory path and resource set.
	 * 
	 * @param modelClassQualifiedName The qualified name of the specified class representing a txtUML model.
	 * @param path The given output directory path.
	 * @param resourceSet The given resource set.
	 * @throws ImportException
	 *
	 * @author Ádám Ancsin
	 */
	public static void createProfileForModel(String modelClassQualifiedName, String path, ResourceSet resourceSet) 
			throws ImportException
	{
		Profile profile = createProfile(modelClassQualifiedName, path);
		Model umlMetamodel = loadUMLMetamodelAndPrimitiveTypes(profile, resourceSet);
		createExternalClassStereotypeForProfile(profile, umlMetamodel);
		defineAndSaveProfile(profile, modelClassQualifiedName, resourceSet);
	}	
	
	/**
	 * Defines and saves the specified profile.
	 * 
	 * @param profile The specified profile.
	 * @param modelClassQualifiedName The qualified name of the class representing a txtUML model.
	 * @param resourceSet The resource set.
	 * @throws ImportException
	 *
	 * @author Ádám Ancsin
	 */
 	private static void defineAndSaveProfile(Profile profile,String modelClassQualifiedName, ResourceSet resourceSet) 
 			throws ImportException
 	{
 		profile.define();
		Resource resource = 
				resourceSet.createResource(
						URI.createFileURI("").
						appendSegment(modelClassQualifiedName).
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
 	
 	/**
 	 * Creates the ExternalClass stereotype for the specified profile using the given UML metamodel.
 	 * @param profile The specified profile.
 	 * @param umlMetamodel The given UML metamodel.
 	 *
 	 * @author Ádám Ancsin
 	 */
	private static void createExternalClassStereotypeForProfile(Profile profile, Model umlMetamodel)
	{
		//creating the ExternalClass stereotype and an extension for it
		try
		{
			org.eclipse.uml2.uml.Class classifierMetaclass = 
					(org.eclipse.uml2.uml.Class) umlMetamodel.getOwnedType("Classifier");

			profile.createMetaclassReference(classifierMetaclass);

			Stereotype externalStereotype=profile.createOwnedStereotype(ImporterConfiguration.externalClassStereotypeName, false);
			externalStereotype.createExtension(classifierMetaclass,false);
		}
		catch(Exception e)
		{

		}
	}
	
	/**
	 * Loads the UML metamodel and Primitive Types Library to the specified profile with the given resource set.
	 * @param profile The specified profile.
	 * @param resourceSet The given resource set.
	 * @return The UML metamodel.
	 *
	 * @author Ádám Ancsin
	 */
	private static Model loadUMLMetamodelAndPrimitiveTypes(Profile profile,ResourceSet resourceSet)
	{
		//loading the UML metamodel
		Model umlMetamodel = (Model)
				loadResource(
						URI.createURI(UMLResource.UML_METAMODEL_URI),
						resourceSet
				);

		//loading UML Primitive Types Library and importing the primitive types from there
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
	
	/**
	 * Creates the profile for a model represented by the specified class with
	 * the given qualified name in a given output directory path.
	 * 
	 * @param modelClassQualifiedName The qualified name of the specified class representing a txtUML model.
	 * @param path The given output directory path. 
	 * 
	 * @return The created profile-
	 *
	 * @author Ádám Ancsin
	 */
	private static Profile createProfile(String modelClassQualifiedName, String path)
	{
		Profile profile = UMLFactory.eINSTANCE.createProfile();
		profile.setName(ImporterConfiguration.profileName);
		profile.setURI(
				URI.createFileURI(path).
				appendSegment(modelClassQualifiedName).
				appendFileExtension(UMLResource.PROFILE_FILE_EXTENSION).
				toString()
		);
		
		return profile;
	}
	
	/**
	 * Loads a resource (an UML2 package) from a specified URI in the given resource set. 
	 * @param uri The specified URI.
	 * @param resourceSet The given resource set.
	 * @return The loaded package.
	 *
	 * @author Ádám Ancsin
	 */
	private static org.eclipse.uml2.uml.Package loadResource(URI uri, ResourceSet resourceSet)
	{
		Resource resource=resourceSet.getResource(uri,true);
		return (org.eclipse.uml2.uml.Package) EcoreUtil.getObjectByType(resource.getContents(),UMLPackage.Literals.PACKAGE);
	}
}
