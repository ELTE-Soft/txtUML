package txtuml.export.uml2;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;

import txtuml.importer.Importer;


public class ExportUML2 {
	public static void main(String[] args) {
		if(args.length != 2) {
			System.out.println("Two command line arguments needed: model class, output file");
			return;
		}
		String modelClassName = args[0];
		String outputName = args[1];
		try {
			org.eclipse.uml2.uml. Model m = Importer.importModel(modelClassName);
			ResourceSet resourceSet = new ResourceSetImpl();
			UMLResourcesUtil.init(resourceSet);
			URI uri = URI.createFileURI(outputName).appendSegment(modelClassName).appendFileExtension(UMLResource.FILE_EXTENSION);
	        Resource resource = resourceSet.createResource(uri);
	        resource.getContents().add(m);
            resource.save(null); // no save options needed
            
		}
		catch(Exception e) 
		{
			System.out.println("Error: " + e.getMessage());
		}
	}
}
