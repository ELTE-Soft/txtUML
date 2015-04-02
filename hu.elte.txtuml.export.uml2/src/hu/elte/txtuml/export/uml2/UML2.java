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
import hu.elte.txtuml.export.uml2.utils.ElementFinder;

/**
 * This class is responsible for exporting Eclipse UML2 model generated from a txtUML model.
 * 
 * @author Ádám Ancsin
 *
 */
public class UML2 {
	
	/**
	 * Exports UML2 model generated from a txtUML model.
	 * 
	 * @param modelClassName 	The fully qualified name of the class representing the txtUML model.
	 * 						 	(e.g. hu.elte.txtuml.examples.machine.MachineModel)
	 * @param outputDirectory 	The name of the output directory. 
	 * 						 	(relative to the path of the project containing the txtUML model)
	 * @throws Exception
	 * 
	 * @author Ádám Ancsin
	 */
	public static void exportModel(String modelClassName, String outputDirectory) throws Exception
	{
		try {
			Class<?> modelClass = ElementFinder.findModel(modelClassName);
			exportModel(modelClass, outputDirectory);
		}
		catch(Exception e) 
		{
			throw e;
		}
	}
	
	/**
	 * Exports UML2 model generated from a txtUML model.
	 * 
	 * @param modelClass 		The class representing the txtUML model.
	 * 
	 * @param outputDirectory 	The name of the output directory. 
	 * 						 	(relative to the path of the project containing the txtUML model)
	 * @throws Exception
	 * 
	 * @author Ádám Ancsin
	 */
	public static void exportModel(Class<?> modelClass, String outputDirectory) throws Exception
	{
		try {
			//import model
			org.eclipse.uml2.uml. Model m = ModelImporter.importModel(modelClass,outputDirectory);
			
			//obtain resource of model
			ResourceSet resourceSet=ModelImporter.getResourceSet();
			Resource resource=null;
			for(Resource r:resourceSet.getResources())
			{
				if(r.getContents().contains(m))
				{
					resource=r;
				}
			}
			//save model
            resource.save(null); // no save options needed
            
            //create resource for profile and save profile
            Profile profile=ModelImporter.getProfile();
            resource = resourceSet.createResource(URI.createURI(outputDirectory).appendSegment("Profile_"+m.getName()).appendFileExtension(UMLResource.PROFILE_FILE_EXTENSION));
    	    resource.getContents().add(profile);
    	    resource.save(null);
    	    
    	    //delete surplus profile
    	    if(!outputDirectory.isEmpty())
    	    {
    	    	Path path=FileSystems.getDefault().getPath("Profile_"+m.getName()+".profile.uml");
        	    Files.delete(path);
    	    }
		}
		catch(Exception e) 
		{
			throw e;
		}
	}
	
	/**
	 * Main method for the class, provides opportunity for using model export as a Java application.
	 * @param args 	Program arguments
	 * 				1st argument: modelClassName
	 * 				2nd argument: outputDirectory
	 * 				(see exportModel method)
	 * @author Ádám Ancsin
	 */
	public static void main(String[] args) {
		if(args.length != 2) {
			System.out.println("Two command line arguments needed: model class, output directory (relative path).");
			return;
		}
		String modelClassName = args[0];
		String outputDirectory = args[1];
		
		System.out.println("Exporting model: "+modelClassName);
		try
		{	exportModel(modelClassName, outputDirectory);
			System.out.println("Model exportation was successful");
		}
		catch(Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		
		System.exit(0);
	}
}
