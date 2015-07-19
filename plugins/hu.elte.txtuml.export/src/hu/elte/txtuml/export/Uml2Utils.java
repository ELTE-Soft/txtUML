package hu.elte.txtuml.export;

/***********************************************************
 * Author: Hack János
 * Version 0.9 2014.02.25
 * Email:zodiakus (at) elte.hu
 **********************************************************/

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;

/**
 * Utilites class for UML2 models
 *
 * @author András Dobreff
 */
public class Uml2Utils {
	
	  public static Model loadModel(String modelUri) throws WrappedException
	  {
	    return loadModel(URI.createPlatformResourceURI(modelUri, false));
	  }
	  
	  public static Model loadModel(URI modelUri) throws WrappedException
	  {
	    ResourceSet resSet = new ResourceSetImpl();
	    resSet.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);

	    resSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
	    Map<URI, URI> uriMap = resSet.getURIConverter().getURIMap();
		URI _uri =URI.createURI(org.eclipse.uml2.uml.resources.ResourcesPlugin.INSTANCE.getBaseURL().getPath());
		uriMap.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP), _uri.appendSegment("libraries").appendSegment(""));//make the load of the type names from the .uml file possible
		UMLResourcesUtil.init(resSet);
	    
	    // Get the resource
	    Resource resource = resSet.getResource(modelUri, true);
	    // Get the first model element and cast it to the right type, in my
	    // example everything is hierarchical included in this first node
	    Model model = (Model) resource.getContents().get(0);
	    return model;
	  }

}
