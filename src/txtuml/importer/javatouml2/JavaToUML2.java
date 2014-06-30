package txtuml.importer.javatouml2;

import org.eclipse.emf.common.util.*;
import org.eclipse.emf.ecore.resource.*;
import org.eclipse.emf.ecore.resource.impl.*;
import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resources.util.*;

import txtuml.importer.ImportException;

import java.lang.Class;

public class JavaToUML2 {
	public static void main(String[] args) {
		if(args.length != 2) {
			System.out.println("Two command line arguments needed: model class, output file");
			return;
		}
		String modelClassName = args[0];
		String outputName = args[1];
		try {
			org.eclipse.uml2.uml. Model m = importModel(modelClassName);
			ResourceSet resourceSet = new ResourceSetImpl();
			UMLResourcesUtil.init(resourceSet);
			URI uri = URI.createFileURI(outputName).appendSegment(modelClassName).appendFileExtension(UMLResource.FILE_EXTENSION);
	        Resource resource = resourceSet.createResource(uri);
	        resource.getContents().add(m);
            resource.save(null); // no save options needed
		} catch(Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	public static Model importModel(String className) throws ImportException {
		// TODO unused
		//Class<?> modelClass = findModel(className);
		Model model = UMLFactory.eINSTANCE.createModel();
        model.setName(className);
        return model;
	}
	
    static Class<?> findModel(String className) throws ImportException {
		try {
			return Class.forName(className);
		} catch(ClassNotFoundException e) {
			throw new ImportException("Cannot find class: " + className);
		}
    }

}