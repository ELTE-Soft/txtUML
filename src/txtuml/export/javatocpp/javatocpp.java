package txtuml.export.javatocpp;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;

import txtuml.importer.ModelImporter;
import txtuml.export.uml2tocpp.Uml2ToCpp;

public class javatocpp {
	public static void main(String[] args) {
		if(args.length != 2) {
			System.out.println("Two command line arguments needed: model class, output directory");
			return;
		}
		
		String modelClassName = args[0];
		String outputName = args[1];
		try {
			org.eclipse.uml2.uml. Model m = ModelImporter.importModel(modelClassName,outputName);
			ResourceSet resourceSet = new ResourceSetImpl();
			UMLResourcesUtil.init(resourceSet);
			URI uri = URI.createFileURI(outputName).appendSegment(modelClassName).appendFileExtension(UMLResource.FILE_EXTENSION);
	        Resource resource = resourceSet.createResource(uri);
	        resource.getContents().add(m);
            resource.save(null); // no save options needed
            
            Uml2ToCpp.buildCppCode(m,args[1]);
		}
		catch(Exception e) 
		{
			System.out.println("Error: " + e.getMessage());
		}
	}
}

