package txtuml.export.uml2;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.resource.UMLResource;

import txtuml.importer.ModelImporter;


public class ExportUML2 {
	public static void main(String[] args) {
		if(args.length != 2) {
			System.out.println("Two command line arguments needed: model class, output file");
			return;
		}
		String modelClassName = args[0];
		String outputName = args[1];
		System.out.println("Exporting model: "+modelClassName);
		try {
			org.eclipse.uml2.uml. Model m = ModelImporter.importModel(modelClassName,outputName);
			ResourceSet resourceSet=ModelImporter.getResourceSet();
			Resource resource=null;
			for(Resource r:resourceSet.getResources())
			{
				if(r.getContents().contains(m))
				{
					resource=r;
				}
			}
            resource.save(null); // no save options needed
            
            Profile profile=ModelImporter.getProfile();
            resource = resourceSet.createResource(URI.createURI(outputName).appendSegment("Profile_"+m.getName()).appendFileExtension(UMLResource.PROFILE_FILE_EXTENSION));
    	    resource.getContents().add(profile);
    	    resource.save(null);
    	    System.out.println("Model exportation was successful");
		}
		catch(Exception e) 
		{
			System.out.println("Error: " + e.getMessage());
		}
		System.exit(0);
	}
}
