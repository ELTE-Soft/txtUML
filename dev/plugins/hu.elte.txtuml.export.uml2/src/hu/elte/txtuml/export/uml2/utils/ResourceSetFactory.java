package hu.elte.txtuml.export.uml2.utils;

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;

import hu.elte.txtuml.export.uml2.backend.ExporterConfiguration;

/**
 * A factory for creating and initializing resource sets that can be used by the
 * exporter.
 */
public final class ResourceSetFactory {

	public ResourceSetFactory() {
	}

	/**
	 * Creates and initializes a resource set.
	 * 
	 * @return The created and initialized resource set.
	 */
	public ResourceSet createAndInitResourceSet() {
		ResourceSet resourceSet = new ResourceSetImpl();

		URI uml2ResourcesPluginURI = URI.createURI(ExporterConfiguration.UML2_RESOURCES_PLUGIN_PATH);
		resourceSet.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION,
				UMLResource.Factory.INSTANCE);

		Map<URI, URI> uriMap = resourceSet.getURIConverter().getURIMap();

		uriMap.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP),
				uml2ResourcesPluginURI.appendSegment("libraries").appendSegment(""));

		uriMap.put(URI.createURI(UMLResource.METAMODELS_PATHMAP),
				uml2ResourcesPluginURI.appendSegment("metamodels").appendSegment(""));

		uriMap.put(URI.createURI(UMLResource.PROFILES_PATHMAP),
				uml2ResourcesPluginURI.appendSegment("profiles").appendSegment(""));

		uriMap.put(URI.createURI("pathmap://TXTUML_STDLIB/"),
				URI.createURI("platform:/plugin/hu.elte.txtuml.stdlib/src/hu/elte/txtuml/stdlib/"));

		UMLResourcesUtil.init(resourceSet);
		return resourceSet;
	}
}
