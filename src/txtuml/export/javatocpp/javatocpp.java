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
	
	private static final String HelpAOption="-h";
	private static final String HelpBOption="-help";
	
	public static void main(String[] args) {
		
		if(args.length < 2) 
		{
			System.out.println("Missing arguments for more information use -h or -help");
			return;
		}
		
		if(args[0].equals(HelpAOption) || args[0].equals(HelpBOption))
		{
			help();
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
            
            Uml2ToCpp.buildCppCode(m,args);
		}
		catch(Exception e) 
		{
			System.out.println("Error: " + e.getMessage());
		}
	}

	private static void help() 
	{
		System.out.println("Usage: javatocpp txtUMLModelPath outputDirectoryPath [options] ...\n"+
							HelpAOption+","+HelpBOption+"\tPrint this message and exit.\n"+
							Uml2ToCpp.helpArgs());
	}
}

