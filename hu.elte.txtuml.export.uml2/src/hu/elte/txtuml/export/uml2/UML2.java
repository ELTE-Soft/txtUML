package hu.elte.txtuml.export.uml2;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.resource.UMLResource;

import hu.elte.txtuml.export.uml2.transform.ModelImporter;

/**
 * This class is responsible for exporting Eclipse UML2 model generated from a txtUML model.
 * 
 * @author Adam Ancsin
 *
 */
public class UML2 
{
	/**
	 * Exports UML2 model generated from a txtUML model.
	 * 
	 * @param modelClass 		The class representing the txtUML model.
	 * 
	 * @param outputDirectory 	The name of the output directory. 
	 * 						 	(relative to the path of the project containing the txtUML model)
	 * @throws Exception
	 * 
	 * @author Adam Ancsin
	 */
	public static void exportModel(Class<?> modelClass, String outputDirectory) throws Exception
	{
		ModelImporter.importModel(modelClass,outputDirectory); 	//import model
		
		ResourceSet resourceSet = ModelImporter.getResourceSet();
		
		Resource modelResource = ModelImporter.getModelResource();
		modelResource.save(null); // no save options needed
        
        //create resource for profile and save profile
        Profile profile=ModelImporter.getProfile();
        Resource profileResource = resourceSet.createResource(
        		URI.createURI(outputDirectory).
        		appendSegment(modelClass.getCanonicalName()).
        		appendFileExtension(UMLResource.PROFILE_FILE_EXTENSION)
        	);
	    profileResource.getContents().add(profile);
	    profileResource.save(null); // no save options needed
	    
	    //delete surplus profile
	    if(!outputDirectory.isEmpty())
	    {
	    	Path path=FileSystems.getDefault().getPath(modelClass.getCanonicalName() + "." + UMLResource.PROFILE_FILE_EXTENSION);
    	    Files.delete(path);
	    }
	}
}
