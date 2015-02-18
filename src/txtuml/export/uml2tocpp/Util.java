package txtuml.export.uml2tocpp;


import static java.lang.System.out;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.Resource.Factory.Registry;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.internal.resource.UMLResourceFactoryImpl;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;



public class Util {
	
	public static class Pair<L,R> {

		  private final L left;
		  private final R right;

		  public Pair(L left, R right) {
		    this.left = left;
		    this.right = right;
		  }

		  public L getKey() { return left; }
		  public R getValue() { return right; }

		  @Override
		  public int hashCode() { return left.hashCode() ^ right.hashCode(); }

		  @Override
		  public boolean equals(Object o) {
		    if (o == null) return false;
		    if (!(o instanceof Pair)) return false;
		    Pair pairo = (Pair) o;
		    return this.left.equals(pairo.getKey()) &&
		           this.right.equals(pairo.getValue());
		  }

		}
	
	  public static Model loadModel(String modelUri) 
	  {

	    ResourceSet resSet = new ResourceSetImpl();
	    resSet.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);

	    resSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
	    Map<URI, URI> uriMap = resSet.getURIConverter().getURIMap();
		URI _uri = URI.createURI("jar:file:umlplugins/org.eclipse.uml2.uml.resources_4.1.0.v20140202-2055.jar!/"); // for example
		uriMap.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP), _uri.appendSegment("libraries").appendSegment(""));//ha nem húzzuk be a típusnevek null-értékûek
		//uriMap.put(URI.createURI(UMLResource.METAMODELS_PATHMAP), _uri.appendSegment("metamodels").appendSegment(""));//passz, egyenlõre nincs hiba
		//uriMap.put(URI.createURI(UMLResource.PROFILES_PATHMAP), _uri.appendSegment("profiles").appendSegment(""));//passz, egyenlõre nincs hiba
		
		UMLResourcesUtil.init(resSet);
	    
	    // Get the resource
	    Resource resource = resSet.getResource(URI
	        .createURI(modelUri), true);
	    // Get the first model element and cast it to the right type, in my
	    // example everything is hierarchical included in this first node
	    Model model = (Model) resource.getContents().get(0);
	    return model;
	  }

}
